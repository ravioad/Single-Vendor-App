package com.example.singlevendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.toast

class OrderExpandableListAdapter(
    val context: Context,
    val dataHeader: ArrayList<String>,
    val childData: HashMap<String, ArrayList<CartItemModel>>
) :
    BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return dataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childData[dataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return dataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childData.get(dataHeader[groupPosition])!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convert_View = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convert_View == null) {
            val inflator =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convert_View = inflator.inflate(R.layout.order_list_header_item, null)
        }
        val headerTextView = convert_View!!.findViewById<TextView>(R.id.order_header_title)
        val headerQuantityTextView = convert_View.findViewById<TextView>(R.id.order_header_quantity)
        val total = getTotalCount(childData[dataHeader[0]])
        headerQuantityTextView.text = total.toString()
        headerTextView.text = "Items"
        return convert_View
    }

    private fun getTotalCount(list: ArrayList<CartItemModel>?): Int {
        var count = 0
        list?.forEach {
            count += it.quantity
        }
        return count
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convert_View = convertView
        val child = getChild(groupPosition, childPosition) as CartItemModel
        if (convert_View == null) {
            val inflator =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convert_View = inflator.inflate(R.layout.order_list_child_item, null)
        }
        val childText = convert_View?.findViewById<TextView>(R.id.order_child_title)
        val childQuantity = convert_View?.findViewById<TextView>(R.id.order_child_quantity)
        childText?.text = child.name
        childQuantity?.text = child.quantity.toString()
//        convert_View?.setOnClickListener{
//            context.toast(child.name)
//        }
        return convert_View!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}