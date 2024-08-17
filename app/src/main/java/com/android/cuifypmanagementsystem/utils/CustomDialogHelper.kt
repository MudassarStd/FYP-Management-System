import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.android.cuifypmanagementsystem.R
import com.google.android.material.textview.MaterialTextView

object CustomDialogHelper {

    fun showLogoutDialog(context: Context, onLogout: () -> Unit) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.logout_dialog, null)

        // Build the MaterialAlertDialogBuilder
        val builder = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true) // Set to false if you don't want to allow dismissing by clicking outside

        // Create the AlertDialog
        val dialog = builder.create()

        // Initialize buttons from the dialog layout
        val btnLogout: Button = dialogView.findViewById(R.id.btnLogoutAdminDialog)
        val btnCancel: Button = dialogView.findViewById(R.id.btnCancelAdminDialog)

        // Set onClick listeners for the buttons
        btnLogout.setOnClickListener {
            onLogout()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    fun showLoadingDialog(context: Context) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.loading_dialog, null)

        // Build the MaterialAlertDialogBuilder
        val builder = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true) // Set to false if you don't want to allow dismissing by clicking outside

        // Create the AlertDialog
        val dialog = builder.create()


        // Show the dialog
        dialog.show()
    }
      fun showActionSuccessDialog(context: Context, messageStr : String) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.action_success_dialog, null)

        // Build the MaterialAlertDialogBuilder
        val builder = MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setCancelable(true) // Set to false if you don't want to allow dismissing by clicking outside

        // Create the AlertDialog
        val dialog = builder.create()

          val message: MaterialTextView = dialogView.findViewById(R.id.tvActionSuccessDialogMessage)
          message.text = messageStr

        // Show the dialog
        dialog.show()
    }



}
