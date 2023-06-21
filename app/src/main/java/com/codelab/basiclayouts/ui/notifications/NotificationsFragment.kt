package com.codelab.basiclayouts.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codelab.basiclayouts.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {
    // Fragment class for the Notifications screen

    private var _binding: FragmentNotificationsBinding? = null
    // View binding property for accessing the views in the fragment

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    // Non-null access to the binding property

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Called when the fragment's UI is being created

        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        // Create an instance of NotificationsViewModel using ViewModelProvider

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        // Inflate the fragment's layout using view binding
        val root: View = binding.root
        // Get the root view of the fragment

        val textView: TextView = binding.textNotifications
        // Reference to the TextView defined in the fragment's layout

        notificationsViewModel.text.observe(viewLifecycleOwner) {
            // Observe the text LiveData from the NotificationsViewModel
            textView.text = it
            // Update the TextView's text with the observed value
        }
        return root
        // Return the root view as the fragment's UI
    }

    override fun onDestroyView() {
        // Called when the fragment's UI is being destroyed
        super.onDestroyView()
        _binding = null
        // Clear the binding reference to avoid memory leaks
    }
}