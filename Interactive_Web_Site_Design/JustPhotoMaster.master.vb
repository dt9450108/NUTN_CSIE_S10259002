Partial Class JustPhotoMaster
    Inherits System.Web.UI.MasterPage

    Protected Sub jpt_masterPageLogout_Click(sender As Object, e As EventArgs) Handles jpt_masterPageLogout.Click
        'clear session
        Session("jpt_id") = ""
        Session("jpt_memberAcc") = ""
        Session("jpt_memberHeadPic") = ""
        Session("jpt_memberHeadPicSelected") = "0"
        Session("jpt_memberName") = ""
        Session("jpt_memberDescrip") = ""
        Session("isLoginState") = ""

        'change master page link and button visible
        headPicture.Visible = False
        jpt_masterPageSignup.Visible = True
        jpt_masterPageLogin.Visible = True
        jpt_masterPageAccount.Visible = False
        jpt_masterPagePersonal.Visible = False
        jpt_masterPageUpload.Visible = False
        jpt_masterPageLogout.Visible = False

        'redirect to default
        Response.Redirect("~\Default.aspx")
    End Sub

    Protected Sub mpHeadLogoBtn_Click(sender As Object, e As EventArgs) Handles mpHeadLogoBtn.Click
        If Session("isLoginState") = "OK" Then
            Response.Redirect("~\Home.aspx")
        Else
            Response.Redirect("~\Default.aspx")
        End If
    End Sub

    Protected Sub jpt_masterPageSignup_Click(sender As Object, e As EventArgs) Handles jpt_masterPageSignup.Click
        Response.Redirect("~\Signup.aspx")
    End Sub

    Protected Sub jpt_masterPageLogin_Click(sender As Object, e As EventArgs) Handles jpt_masterPageLogin.Click
        Response.Redirect("~\Login.aspx")
    End Sub

    Protected Sub jpt_masterPageAccount_Click(sender As Object, e As EventArgs) Handles jpt_masterPageAccount.Click
        Response.Redirect("~\UserInfo.aspx")
    End Sub

    Protected Sub jpt_masterPagePersonal_Click(sender As Object, e As EventArgs) Handles jpt_masterPagePersonal.Click
        Response.Redirect("~\Personal.aspx")
    End Sub

    Protected Sub headPicture_Click(sender As Object, e As ImageClickEventArgs) Handles headPicture.Click
        Response.Redirect("~\UserHeadPic.aspx")
    End Sub

    Protected Sub jpt_masterPageUpload_Click(sender As Object, e As EventArgs) Handles jpt_masterPageUpload.Click
        Response.Redirect("~\UploadPicture.aspx")
    End Sub
End Class