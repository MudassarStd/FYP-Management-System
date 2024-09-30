package com.android.cuifypmanagementsystem.student.adapter.recyclerview


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemGroupMemberSelectionBinding
import com.android.cuifypmanagementsystem.student.datamodel.GroupMember

class GroupMembersAdapter(
    private var members: List<GroupMember>,
    private val memberClickListener: OnGroupMemberSelectionInterface // Interface listener
) : RecyclerView.Adapter<GroupMembersAdapter.GroupMembersViewHolder>() {

    interface OnGroupMemberSelectionInterface {
        fun onMemberRemoved(member : GroupMember)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersViewHolder {
        val binding = RvItemGroupMemberSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GroupMembersViewHolder(binding, memberClickListener)
    }

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int) {
        val member = members[position]
        holder.bind(member)

    }

    override fun getItemCount(): Int = members.size

    fun updateMembers(members: List<GroupMember>) {
        this.members = members
        notifyDataSetChanged()
    }

    class GroupMembersViewHolder(
        val binding: RvItemGroupMemberSelectionBinding,
        val memberClickListener: OnGroupMemberSelectionInterface
    ) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(member : GroupMember) {
           binding.tvGroupMemberName.text = member.name
           binding.tvGroupMemberRegistration.text = member.registrationNumber

            binding.ivRemoveMember.setOnClickListener {
                memberClickListener.onMemberRemoved(member = member)
            }
        }
    }
}