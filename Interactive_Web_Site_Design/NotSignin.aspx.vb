
Partial Class NotSignin
    Inherits System.Web.UI.Page

    Protected Sub jpt_notSigninPageLogin_Click(sender As Object, e As EventArgs) Handles jpt_notSigninPageLogin.Click
        Response.Redirect("~\Login.aspx")
    End Sub
End Class
