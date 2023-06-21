package com.codelab.basiclayouts.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codelab.basiclayouts.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null // Binding object for accessing views
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!! // Property to access _binding, throws an exception if _binding is null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java) // Create an instance of DashboardViewModel

        _binding = FragmentDashboardBinding.inflate(inflater, container, false) // Inflate the fragment's layout
        val root: View = binding.root // Get the root view from the binding object

        val textView: TextView = binding.textDashboard // Get the TextView with id textDashboard from the binding object
        dashboardViewModel.text.observe(viewLifecycleOwner) { // Observe the text property of the dashboardViewModel
            textView.text = it // Update the text of the textView
        }
        return root // Return the root view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding object to avoid memory leaks
    }
}