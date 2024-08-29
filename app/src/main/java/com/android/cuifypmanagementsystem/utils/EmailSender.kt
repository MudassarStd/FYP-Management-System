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

    private const val TAG = "EmailSender" // Tag for log messages

    private const val senderEmail = "mudassarstd@gmail.com"
    private const val senderPassword = "kxmu onnn shxa blef"
    private const val host = "smtp.gmail.com"

    private val properties = Properties().apply {
        put("mail.smtp.host", host)
        put("mail.smtp.port", "465")
        put("mail.smtp.ssl.enable", "true")
        put("mail.smtp.auth", "true")
    }

    private fun createSession(): Session {
        return Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })
    }

    suspend fun sendRegistrationEmail(
        recipient: String,
        tempPassword: String,
        name: String
    ) = withContext(Dispatchers.IO) {
        try {
            val session = createSession()
            Log.d(TAG, "Session created successfully for registration email.")

            val messageContent = """
                Hi $name,
                
                You have been registered to CUI FYP Management System.
                
                Login with the following credentials:
                Email: $recipient
                Password: $tempPassword
                
                Note: You must change your password after the first successful login.
            """.trimIndent()

            sendEmail(session, recipient, "FYP Management System Registration", messageContent)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending registration email: ${e.message}")
        }
    }

    suspend fun sendForgotPasswordEmail(
        recipientEmail: String,
        resetLink: String
    ) = withContext(Dispatchers.IO) {
        try {
            val session = createSession()
            Log.d(TAG, "Session created successfully for forgot password email.")

            val messageContent = """
                Hi,
                
                We received a request to reset your password.
                
                Please click the link below to reset your password:
                
                
                If you didn't request this, please ignore this email.
            """.trimIndent()

            sendEmail(session, recipientEmail, "FYP Management System Password Reset", messageContent)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending forgot password email: ${e.message}")
        }
    }

    private fun sendEmail(session: Session, recipient: String, subject: String, content: String) {
        try {
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                setSubject(subject)
                setText(content)
            }

            Log.d(TAG, "Email message composed: $subject")
            Transport.send(mimeMessage)
            Log.d(TAG, "Email sent successfully to $recipient.")
        } catch (e: AddressException) {
            Log.e(TAG, "Invalid email address: ${e.message}")
        } catch (e: MessagingException) {
            Log.e(TAG, "Error sending email: ${e.message}")
        }
    }
}
