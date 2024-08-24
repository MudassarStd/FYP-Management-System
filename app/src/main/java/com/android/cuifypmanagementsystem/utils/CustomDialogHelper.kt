import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.android.cuifypmanagementsystem.R
import com.android.cuifypmanagementsystem.admin.activities.AdminDashboardActivity

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

    fun showActionSuccessDialog(context: Context, messageStr: String) {
        // Inflate the custom layout

        // Build the MaterialAlertDialogBuilder
        val builder = MaterialAlertDialogBuilder(context)
            .setTitle("Action Successful") // Set the title of the dialog
            .setMessage(messageStr) // Set the message
            .setPositiveButton("Ok") { dialog, _ ->
                // Handle positive button click

                dialog.dismiss()
                context.startActivity(Intent(context, AdminDashboardActivity::class.java))
            }
            .setCancelable(true) // Set to false if you don't want to allow dismissing by clicking outside

        // Create the AlertDialog
        val dialog = builder.create()

        // Show the dialog
        dialog.show()
    }

    fun showReversibleActionFailedDialog(
        context: Context,
        messageStr: String,
        onRetry: () -> Unit,
        onCancel: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Failed") // Set the title of the dialog
            .setMessage(messageStr) // Set the message
            .setPositiveButton("Retry") { dialog, _ ->
                // Handle positive button click
                onRetry()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // Handle negative button click
                onCancel()
                dialog.dismiss()
            }

            .show()
    }

    fun showActionConfirmationDialog(context: Context, messageStr: String, onProceed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Confirm Action") // Set the title of the dialog
            .setMessage(messageStr) // Set the message
            .setPositiveButton("Proceed") { dialog, _ ->
                // Handle positive button click
                onProceed()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->

                dialog.dismiss()
            }

            .show()
    }

}


