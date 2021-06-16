package com.angogasapps.myfamily.ui.screens.family_storage.dialogs

import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.widget.LinearLayout
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.createFolder
import es.dmoral.toasty.Toasty

class NameGetterDialog {
    val context: Context

    constructor(context: Context) {
        this.context = context
    }

     fun show(isFolder: Boolean, name: String = "", function: (name: String) -> Unit) {
         val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)


         alertDialog.setTitle(context.getString(if (isFolder )R.string.folder_name else R.string.file_name))


         val input = EditText(context)
         input.setText(name)
         val lp = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT)
         input.layoutParams = lp


         alertDialog.setView(input)

         alertDialog.setNegativeButton(context.getString(R.string.cancel)) { dialog: DialogInterface, which: Int -> dialog.cancel() }

         alertDialog.setPositiveButton(context.getString(R.string.save)) { dialog: DialogInterface?, which: Int ->
             val name = input.text.toString().trim()
             if (name != null && name != "") {
                 function(name)
             }else{
                 Toasty.warning(context,
                         context.getString(if (isFolder)R.string.folder_name_is_empty else R.string.file_name_is_empty)).show()
             }
             dialog?.dismiss()
         }
         alertDialog.show()
    }
}