package com.example.homework3

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.example.homework3.databinding.ItemContactBinding
import com.example.homework3.model.ContactView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

interface ContactAdapterCallbacks {
    fun onCheckBoxClick(contact: ContactView)
    fun onContactMoveUp(contact: ContactView)
    fun onContactMoveDown(contact: ContactView)
    fun onContactEdit(contact: ContactView)
    fun onContactSave(contact: ContactView)
    fun onContactRemove(contact: ContactView)
}

class ContactAdapter(
    callbacks: ContactAdapterCallbacks
) : AsyncListDifferDelegationAdapter<ContactView>(ContactDiffUtilCallback) {

    private object ContactDiffUtilCallback : DiffUtil.ItemCallback<ContactView>() {
        override fun areItemsTheSame(oldItem: ContactView, newItem: ContactView): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ContactView, newItem: ContactView): Boolean =
            oldItem == newItem

    }

    init {
        delegatesManager.addDelegate(createContactAdapterDelegate(callbacks))
    }

    private fun createContactAdapterDelegate(
        callbacks: ContactAdapterCallbacks
    ) = adapterDelegateViewBinding<ContactView, ContactView, ItemContactBinding>(
        { inflater, parent -> ItemContactBinding.inflate(inflater, parent, false) }
    ) {
        with(binding) {
            checked.setOnClickListener { callbacks.onCheckBoxClick(item) }
            up.setOnClickListener { callbacks.onContactMoveUp(item) }
            down.setOnClickListener { callbacks.onContactMoveDown(item) }
            edit.setOnClickListener {
                if (item.isEditState) {
                    val updatedItem = item.copy(
                        name = binding.name.text.toString(),
                        lastName = binding.lastName.text.toString(),
                        phoneNumber = binding.phone.text.toString()
                    )
                    callbacks.onContactSave(updatedItem)
                } else callbacks.onContactEdit(item)
            }
            delete.setOnClickListener { callbacks.onContactRemove(item) }
        }

        bind {
            binding.name.setText(item.name)
            binding.name.isEnabled = item.isEditState
            binding.lastName.setText(item.lastName)
            binding.lastName.isEnabled = item.isEditState
            binding.phone.setText(item.phoneNumber)
            binding.phone.isEnabled = item.isEditState
            binding.checked.isChecked = item.checked
            val text = if (item.isEditState) "Save" else "Edit"
            binding.edit.text = text
        }
    }
}
