package com.example.homework3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework3.model.Contact
import com.example.homework3.model.ContactView
import com.github.javafaker.Faker
import java.util.Collections

class MainActivityViewModel : ViewModel(), ContactAdapterCallbacks {
    private val faker = Faker.instance()
    private val contacts = (0..100).map {
        Contact(
            id = it.toLong(),
            name = faker.name().firstName(),
            lastName = faker.name().lastName(),
            phoneNumber = faker.phoneNumber().phoneNumber(),
        )
    }.toMutableList()

    private val checkedContacts = mutableSetOf<Long>()
    private var currentEdited = mutableSetOf<Long>()

    private val _list: MutableLiveData<List<ContactView>> = MutableLiveData(emptyList())
    val list: LiveData<List<ContactView>> get() = _list

    init {
        updateList()
    }

    override fun onCheckBoxClick(contact: ContactView) {
        if (checkedContacts.contains(contact.id)) checkedContacts.remove(contact.id)
        else checkedContacts.add(contact.id)
    }

    override fun onContactMoveUp(contact: ContactView) {
        val index = contacts.indexOfFirst { it.id == contact.id }
        if (index == -1 || index == 0) return
        Collections.swap(contacts, index, index - 1)
        updateList()
    }

    override fun onContactMoveDown(contact: ContactView) {
        val index = contacts.indexOfFirst { it.id == contact.id }
        if (index == -1 || index == contacts.lastIndex) return
        Collections.swap(contacts, index, index + 1)
        updateList()
    }

    override fun onContactEdit(contact: ContactView) {
        if (currentEdited.contains(contact.id)) return
        currentEdited.add(contact.id)
        updateList()
    }

    override fun onContactSave(contact: ContactView) {
        if (!currentEdited.contains(contact.id)) return
        val currentIndex = contacts.indexOfFirst { it.id == contact.id }
        val updatedContact = contacts[currentIndex].copy(
            name = contact.name,
            lastName = contact.lastName,
            phoneNumber = contact.phoneNumber
        )
        contacts[currentIndex] = updatedContact
        currentEdited.remove(contact.id)
        updateList()
    }

    override fun onContactRemove(contact: ContactView) {
        contacts.removeIf { it.id == contact.id }
        checkedContacts.removeIf { it == contact.id }
        updateList()
    }

    fun addContact() {
        val newContact = Contact(
            id = contacts.maxOf { it.id } + 1,
            name = "",
            lastName = "",
            phoneNumber = "",
        )
        contacts.add(newContact)
        updateList()
    }

    fun deleteSelected() {
        if (checkedContacts.isEmpty()) return
        checkedContacts.forEach { id ->
            contacts.removeIf { it.id == id }
            currentEdited.removeIf { it == id }
        }
        updateList()
    }

    private fun updateList() {
        _list.value = contacts.map { it.toContactView() }
    }

    private fun Contact.toContactView(): ContactView = ContactView(
        id = id,
        name = name,
        lastName = lastName,
        phoneNumber = phoneNumber,
        checked = checkedContacts.contains(id),
        isEditState = currentEdited.contains(id)
    )

}