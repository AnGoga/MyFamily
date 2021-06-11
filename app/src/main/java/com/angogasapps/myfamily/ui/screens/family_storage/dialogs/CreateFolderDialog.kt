package com.angogasapps.myfamily.ui.screens.family_storage.dialogs

import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.widget.LinearLayout
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.createFolder
import es.dmoral.toasty.Toasty

class CreateFolderDialog {
    val context: Context

    constructor(context: Context) {
        this.context = context
    }

     fun show(rootNode: String, rootFolder: String) {
         val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)


         alertDialog.setTitle(context.getString(R.string.create_folder))


         val input = EditText(context)
         val lp = LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT,
                 LinearLayout.LayoutParams.MATCH_PARENT)
         input.layoutParams = lp


         alertDialog.setView(input)

         alertDialog.setNegativeButton(context.getString(R.string.cancel))
         { dialog: DialogInterface, which: Int -> dialog.cancel() }

         alertDialog.setPositiveButton(context.getString(R.string.create)) { dialog: DialogInterface?, which: Int ->
             val name = input.text.toString().trim()
             if (name != null && name != "") {
                 createFolder(name = name, rootNode = rootNode, rootFolder = rootFolder)
             }else{
                 Toasty.warning(context, context.getString(R.string.folder_name_is_empty)).show()
             }
             dialog?.dismiss()
         }
         alertDialog.show()
    }
}