
Partial Class Signup
    Inherits System.Web.UI.Page
    Protected Sub accountValidator_ServerValidate(source As Object, args As ServerValidateEventArgs) Handles accountValidator.ServerValidate
        Dim accountText As String = TextBoxAccount.Text
        Dim rexUrl As New Regex("[^a-z0-9A-Z]")

        If accountText.Length < 6 And accountText.Length > 0 Then
            accountValidator.ErrorMessage = "至少輸入六碼"
            args.IsValid = False
            Exit Sub
        ElseIf rexUrl.IsMatch(TextBoxAccount.Text) Then
            accountValidator.ErrorMessage = "帳號請輸入數字與英文"
            args.IsValid = False
            Exit Sub
        End If
    End Sub

    Protected Sub pwValidator_ServerValidate(source As Object, args As ServerValidateEventArgs) Handles pwValidator.ServerValidate
        Dim pw As String = TextBoxPassword.Text

        If pw.Length < 6 And pw.Length > 0 Then
            pwValidator.ErrorMessage = "至少六碼的密碼組合"
            args.IsValid = False
            Exit Sub
        End If
    End Sub

    Protected Sub BtnSubmit_Click(sender As Object, e As EventArgs) Handles BtnSubmit.Click
        'get each information from textbox
        Dim account As String = TextBoxAccount.Text
        Dim password As String = TextBoxPassword.Text
        Dim passwordConfirm As String = TextBoxPasswordConfirm.Text
        Dim email As String = TextBoxEmail.Text
        Dim name As String = TextBoxName.Text
        Dim headPictureFileNmae As String = ""

        Dim checkValidator As Boolean = accountRequired.IsValid And accountValidator.IsValid And pwValidator.IsValid And pwRequired.IsValid And pwcRequired.IsValid And pwcCompare.IsValid And _
                emailRequired.IsValid And emailValidator.IsValid And nameRequired.IsValid
        If Not checkValidator Then
            Exit Sub
        End If

        'save sign up information to database
        Dim conn1 As System.Data.SqlClient.SqlConnection
        conn1 = New System.Data.SqlClient.SqlConnection
        conn1.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()
        Dim cmd1 As System.Data.SqlClient.SqlCommand
        cmd1 = New System.Data.SqlClient.SqlCommand
        cmd1.Connection = conn1
        cmd1.CommandType = Data.CommandType.StoredProcedure
        cmd1.CommandText = "CREATEACCOUNT"

        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@account", System.Data.SqlDbType.NVarChar, 15))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@password", System.Data.SqlDbType.NVarChar, 32))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@name", System.Data.SqlDbType.NVarChar, 30))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@email", System.Data.SqlDbType.NVarChar, 50))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@description", System.Data.SqlDbType.NVarChar, 50))
        cmd1.Parameters.Add(New System.Data.SqlClient.SqlParameter("@headPicture", System.Data.SqlDbType.NVarChar, 25))

        cmd1.Parameters("@ID").Value = System.DBNull.Value
        cmd1.Parameters("@account").Value = account
        cmd1.Parameters("@password").Value = GetMd5Hash(System.Security.Cryptography.MD5.Create(), password)
        cmd1.Parameters("@name").Value = name
        cmd1.Parameters("@email").Value = email
        cmd1.Parameters("@description").Value = ""
        cmd1.Parameters("@headPicture").Value = headPictureFileNmae

        conn1.Open()
        Dim dr1 As System.Data.SqlClient.SqlDataReader
        dr1 = cmd1.ExecuteReader
        If dr1.Read Then
            Dim getRetCode As Integer = Integer.Parse(dr1(0).ToString)

            Select Case getRetCode
                Case 0
                    ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('註冊成功！返回首頁');window.location='Default.aspx';", True)
                Case 1
                    Response.Write("<Script language='JavaScript'>alert('帳號已經存在，請修改！');</Script>")
                Case 2
                    Response.Write("<Script language='JavaScript'>alert('信箱已經存在，請修改！');</Script>")
                Case 9
                    Response.Write("<Script language='JavaScript'>alert('資料已經更新！');</Script>")
                Case Else
                    Response.Write("<Script language='JavaScript'>alert('哪邊出錯了？！');</Script>")
            End Select


        End If
        dr1.Close()
        conn1.Close()
    End Sub

    Public Function GetMd5Hash(ByVal md5Hash As System.Security.Cryptography.MD5, ByVal input As String) As String
        Dim data As Byte() = md5Hash.ComputeHash(Encoding.UTF8.GetBytes(input))

        Dim sBuilder As New StringBuilder()

        Dim i As Integer
        For i = 0 To data.Length - 1
            sBuilder.Append(data(i).ToString("X2"))
        Next i

        Return sBuilder.ToString()
    End Function
End Class
