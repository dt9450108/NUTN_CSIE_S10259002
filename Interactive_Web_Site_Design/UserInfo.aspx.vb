
Partial Class UserInfo
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(sender As Object, e As EventArgs) Handles Me.Load
        If Not Session("isLoginState") = "OK" Then
            Response.Redirect("~\NotSignin.aspx")
        Else
            Dim link_masterPageHeadPic As ImageButton = CType(Page.Master.FindControl("headPicture"), ImageButton)
            Dim link_masterPageSignup As Button = CType(Page.Master.FindControl("jpt_masterPageSignup"), Button)
            Dim link_masterPageLogin As Button = CType(Page.Master.FindControl("jpt_masterPageLogin"), Button)
            Dim link_masterPageAccount As Button = CType(Page.Master.FindControl("jpt_masterPageAccount"), Button)
            Dim link_masterPagePersonal As Button = CType(Page.Master.FindControl("jpt_masterPagePersonal"), Button)
            Dim link_masterPageUpload As Button = CType(Page.Master.FindControl("jpt_masterPageUpload"), Button)
            Dim link_masterPageLogout As Button = CType(Page.Master.FindControl("jpt_masterPageLogout"), Button)

            link_masterPageHeadPic.Visible = True
            link_masterPageSignup.Visible = False
            link_masterPageLogin.Visible = False
            link_masterPageAccount.Visible = True
            link_masterPagePersonal.Visible = True
            link_masterPageUpload.Visible = True
            link_masterPageLogout.Visible = True

            link_masterPageAccount.Text = Session("jpt_memberAcc").ToString()
            If Not Session("jpt_memberHeadPic") = "" Then
                link_masterPageHeadPic.ImageUrl = "~/img/hdp/" + Session("jpt_id") + "/" + Session("jpt_memberHeadPic")
            Else
                link_masterPageHeadPic.ImageUrl = "~/img/guest_35_35.png"
            End If
        End If

        If ((Session("isLoginState") = "") Or (Session("jpt_id") = "")) Then
            Response.Redirect("~\NotSignin.aspx")
            Exit Sub
        ElseIf Not (Page.IsPostBack) Then
            Try
                Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
                jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

                Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
                jptCommand.Connection = jptConn
                jptCommand.CommandType = Data.CommandType.StoredProcedure
                jptCommand.CommandText = "GETUSERINFOBYID"

                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
                jptCommand.Parameters("@ID").Value = CType(Session("jpt_id").ToString(), Integer)

                Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

                Try
                    jptConn.Open()
                    jptDataReader = jptCommand.ExecuteReader

                    Dim FoundAccount As Boolean = False

                    If jptDataReader.Read() Then
                        Dim getAccount As String = jptDataReader(0).ToString()
                        Dim getEmail As String = jptDataReader(1).ToString()
                        Dim getName As String = jptDataReader(2).ToString()
                        Dim getDescription As String = jptDataReader(3).ToString()

                        'display unable to edit data and able to edit data
                        userinfo_TBoxAccount.Text = getAccount
                        userinfo_TBoxEmail.Text = getEmail
                        userinfo_TBoxName.Text = getName
                        userinfo_TBoxDescription.Text = getDescription
                        Session("jpt_memberDescrip") = getDescription
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
        End If
    End Sub

    Protected Sub BtnUserinfoCancel_Click(sender As Object, e As EventArgs) Handles BtnUserinfoCancel.Click
        Response.Redirect("~\Home.aspx")
    End Sub

    Protected Sub BtnUserinfoUpdate_Click(sender As Object, e As EventArgs) Handles BtnUserinfoUpdate.Click
        Dim newPassword As String = userinfo_TBoxPassword.Text
        Dim newPasswordConfirm As String = userinfo_TBoxPasswordConfirm.Text
        Dim newDescription As String = userinfo_TBoxDescription.Text

        Dim newPWEmpty As Boolean = (newPassword = String.Empty)
        Dim newPWCEmpty As Boolean = (newPasswordConfirm = String.Empty)

        'if pw less than 7 character
        If Not (pwValidator.IsValid And pwcCompare.IsValid) Then
            Exit Sub
        End If

        If (newPWEmpty And newPWCEmpty And (newDescription = Session("jpt_memberDescrip"))) Then
            ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('資料未更新，導向首頁...');window.location='Home.aspx';", True)
            Exit Sub
        ElseIf (newPWEmpty Or newPWCEmpty) And (newDescription = Session("jpt_memberDescrip")) Then
            CustomValidatorPWCEmpty.IsValid = False
            Exit Sub
        Else
            Try
                Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
                jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

                Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
                jptCommand.Connection = jptConn
                jptCommand.CommandType = Data.CommandType.StoredProcedure
                jptCommand.CommandText = "UPDATEUSERINFOBYID"

                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@password", System.Data.SqlDbType.NVarChar, 32))
                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@description", System.Data.SqlDbType.NVarChar, 50))


                jptCommand.Parameters("@ID").Value = CType(Session("jpt_id").ToString(), Integer)

                'if pw not change
                If newPWEmpty Then
                    ' pw not change
                    jptCommand.Parameters("@password").Value = System.DBNull.Value
                Else
                    jptCommand.Parameters("@password").Value = GetMd5Hash(System.Security.Cryptography.MD5.Create(), newPassword)
                End If
                jptCommand.Parameters("@description").Value = newDescription

                Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

                Try
                    jptConn.Open()
                    jptDataReader = jptCommand.ExecuteReader

                    Dim FoundAccount As Boolean = False

                    If jptDataReader.Read() Then
                        Dim getRetCode As Integer = Integer.Parse(jptDataReader(0).ToString)

                        Select Case getRetCode
                            Case 0
                                Session("jpt_memberDescrip") = newDescription
                                ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('資料更新成功！返回首頁');window.location='Home.aspx';", True)
                            Case 1
                                Response.Write("<Script language='JavaScript'>alert('資料更新失敗！');</Script>")
                            Case Else
                                Response.Write("<Script language='JavaScript'>alert('哪邊出錯了？！');</Script>")
                        End Select
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
        End If
    End Sub

    Protected Sub userinfo_TBoxDescriptionValid_ServerValidate(source As Object, args As ServerValidateEventArgs) Handles userinfo_TBoxDescriptionValid.ServerValidate
        If userinfo_TBoxDescription.Text.Length > 50 Then
            args.IsValid = False
            Exit Sub
        End If
    End Sub

    Protected Sub pwValidator_ServerValidate(source As Object, args As ServerValidateEventArgs) Handles pwValidator.ServerValidate
        Dim pw As String = userinfo_TBoxPassword.Text

        If pw.Length < 6 And pw.Length > 0 Then
            pwValidator.ErrorMessage = "至少六碼的密碼組合"
            args.IsValid = False
            Exit Sub
        End If
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
