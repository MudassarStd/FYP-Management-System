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

    suspend fun sendRegistrationEmail(
        recipient: String,
        tempPassword: String,
        name: String
    ) = withContext(Dispatchers.IO) {
        val senderEmail = "mudassarstd@gmail.com"
        val senderPassword = "password"
        val host = "smtp.gmail.com"

        val properties = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", "465")
            put("mail.smtp.ssl.enable", "true")
            put("mail.smtp.auth", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(senderEmail, senderPassword)
            }
        })

        try {
            val mimeMessage = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                addRecipient(Message.RecipientType.TO, InternetAddress(recipient))
                subject = "FYP Management System Registration"
                setText(
                    "Hi $name,\n\nYou have been registered to CUI FYP Management System.\n\nLogin with the following credentials:" +
                            "\nEmail: $recipient\nPassword: $tempPassword\n\nNote: You must change your password after the first successful login."
                )
            }

            Transport.send(mimeMessage)
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}
