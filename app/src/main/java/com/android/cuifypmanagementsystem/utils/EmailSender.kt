import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailSender {

    private const val TAG = "EmailSenderTesting" // Tag for log messages

    suspend fun sendRegistrationEmail(
        recipient: String,
        tempPassword: String,
        name: String
    ) = withContext(Dispatchers.IO) {
        val senderEmail = "Sender@gmail.com"
        val senderPassword = "password"
        val host = "smtp.gmail.com"

        val properties = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", "465")
            put("mail.smtp.ssl.enable", "true")
            put("mail.smtp.auth", "true")
        }

        try {
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            Log.d(TAG, "Session created successfully.") // Log session creation

            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                subject = "FYP Management System Registration"
                setText(
                    "Hi $name,\n\nYou have been registered to CUI FYP Management System.\n\nLogin with the following credentials:" +
                            "\nEmail: $recipient\nPassword: $tempPassword\n\nNote: You must change your password after the first successful login."
                )
            }

            Log.d(TAG, "Email message composed.") // Log message composition

            Transport.send(mimeMessage)

            Log.d(TAG, "Email sent successfully to $recipient.") // Log successful sending

        } catch (e: AddressException) {
            Log.d(TAG, "Invalid email address: ${e.message}") // Log address errors
        } catch (e: MessagingException) {
            Log.d(TAG, "Error sending email: ${e.message}") // Log messaging errors
        }
    }
}
