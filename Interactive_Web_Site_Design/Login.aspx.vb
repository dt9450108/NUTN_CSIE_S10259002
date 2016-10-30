
Partial Class Login
    Inherits System.Web.UI.Page

    Protected Sub BtnLogin_Click(sender As Object, e As EventArgs) Handles BtnLogin.Click
        Dim loginAccountStr As String = LoginPageAccount.Text
        Dim loginPassword As String = LoginPagePassword.Text

        'Dim checkLoginAll As Boolean = LoginAccountEmpty.IsValid And LoginPasswordEmpty.IsValid
        If Not (LoginAccountEmpty.IsValid And LoginPasswordEmpty.IsValid) Then
            Exit Sub
        End If

        Try
            Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
            jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

            Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
            jptCommand.Connection = jptConn
            jptCommand.CommandType = Data.CommandType.StoredProcedure
            jptCommand.CommandText = "GETACCOUNT"
            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@account", System.Data.SqlDbType.NVarChar, 15))
            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@email", System.Data.SqlDbType.NVarChar, 50))
            jptCommand.Parameters("@account").Value = loginAccountStr
            jptCommand.Parameters("@email").Value = loginAccountStr

            Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

            Try
                jptConn.Open()
                jptDataReader = jptCommand.ExecuteReader

                Dim FoundAccount As Boolean = False

                While jptDataReader.Read()
                    Dim getID As String = jptDataReader(0)
                    Dim getAccount As String = jptDataReader(1)
                    Dim getEmail As String = jptDataReader(2)
                    Dim getPassword As String = jptDataReader(3)
                    Dim getHeadPic As String = jptDataReader(4)
                    Dim getName As String = jptDataReader(5)
                    Dim getDescription As String = jptDataReader(6)

                    'if find account or email
                    If (getAccount = loginAccountStr) Or (getEmail = loginAccountStr) Then
                        'if the password is correct
                        If (VerifyMd5Hash(System.Security.Cryptography.MD5.Create(), loginPassword, getPassword)) Then
                            FoundAccount = True     'represent find account, if not then the account is not registed

                            ' process login success 
                            Session("jpt_id") = getID
                            Session("jpt_memberAcc") = getAccount
                            Session("jpt_memberHeadPic") = getHeadPic
                            Session("jpt_memberName") = getName
                            Session("jpt_memberDescrip") = getDescription
                            Session("isLoginState") = "OK"
                            Response.Redirect("~/Home.aspx")

                            ' login success end
                            Exit While
                        End If
                    End If
                End While

                ' if not find account then represent the account is not regiseted
                If Not FoundAccount Then
                    LoginAccountNotFound.IsValid = False
                    LoginPagePassword.Text = ""
                End If
            Catch ex As Exception
                Response.Write("<Script language='JavaScript'>alert('" & ex.Message & "');</Script>")
            Finally
                If Not (jptDataReader Is Nothing) Then
                    jptCommand.Cancel()
                    jptDataReader.Close()
                End If

                If (jptConn.State = System.Data.ConnectionState.Open) Then
                    jptConn.Close()
                    jptConn.Dispose()
                End If
            End Try
        Catch ex As Exception
            Response.Write("<Script language='JavaScript'>alert('" & ex.Message & "');</Script>")
        End Try
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

    Public Function VerifyMd5Hash(ByVal md5Hash As System.Security.Cryptography.MD5, ByVal input As String, ByVal hash As String) As Boolean
        Dim hashOfInput As String = GetMd5Hash(md5Hash, input)

        Dim comparer As StringComparer = StringComparer.OrdinalIgnoreCase

        If 0 = comparer.Compare(hashOfInput, hash) Then
            Return True
        Else
            Return False
        End If
    End Function
End Class
