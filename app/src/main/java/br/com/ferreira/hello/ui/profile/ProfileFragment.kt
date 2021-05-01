package br.com.ferreira.hello.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.ferreira.hello.databinding.ProfileFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater, container, false)
        viewModel.userId.observe(viewLifecycleOwner, { value ->
            binding.userId.text = value
        })
        viewModel.username.observe(viewLifecycleOwner, { value ->
            binding.username.text = value
        })
        viewModel.fullname.observe(viewLifecycleOwner, { value ->
            binding.fullname.text = value
        })
        viewModel.email.observe(viewLifecycleOwner, { value ->
            binding.email.text = value
        })
        viewModel.message.observe(viewLifecycleOwner, { value ->
            binding.message.text = value
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.title = "Profile"
        viewModel.updateData()
    }

}