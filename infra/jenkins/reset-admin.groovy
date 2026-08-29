import hudson.model.User
import hudson.security.HudsonPrivateSecurityRealm

def user = User.getById("admin", false)

if (user != null) {
    user.addProperty(
        HudsonPrivateSecurityRealm.Details.fromPlainPassword("TempAdmin123!")
    )
    user.save()
}
