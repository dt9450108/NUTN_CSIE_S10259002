
Partial Class UploadPicture
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
    End Sub

    Protected Sub UploadPictureDescriptionValidator_ServerValidate(source As Object, args As ServerValidateEventArgs) Handles UploadPictureDescriptionValidator.ServerValidate
        If UploadPictureDescription.Text.Length > 50 Then
            args.IsValid = False
            Exit Sub
        End If
    End Sub

    Protected Sub BtnUploadPageUpload_Click(sender As Object, e As EventArgs) Handles BtnUploadPageUpload.Click
        ' get necessary information from user
        Dim userId As String = Session("jpt_id")
        Dim userAccount As String = Session("jpt_memberAcc")

        ' dim pic stored path to "~/img/urspics/" & userId & "/"
        Dim pathStr As String = "~/img/urspics/" & userId & "/"
        Dim path As String = Server.MapPath(pathStr)

        ' get pic description
        Dim picState As String = UploadPictureDescription.Text

        If UploadPageFileUpload.HasFile Then
            Try
                ' file extension
                Dim fileExtension As String = System.IO.Path.GetExtension(UploadPageFileUpload.FileName).ToLower()

                'rename file
                Dim imgFileNameWithoutExtension As String = System.IO.Path.GetFileNameWithoutExtension(UploadPageFileUpload.FileName)

                'get upload time
                Dim ForDBDateTime As String = DateTime.Now.Year.ToString + "-" + _
                                        DateTime.Now.Month.ToString + "-" + _
                                        DateTime.Now.Day.ToString + " " + _
                                        DateTime.Now.Hour.ToString + ":" + _
                                        DateTime.Now.Minute.ToString + ":" + _
                                        DateTime.Now.Second.ToString
                Dim currentDateTime As String = ForDBDateTime
                currentDateTime = currentDateTime.Replace("-", "_")
                currentDateTime = currentDateTime.Replace(" ", "_")
                currentDateTime = currentDateTime.Replace(":", "_")

                ' rename without extension
                imgFileNameWithoutExtension = userId & "_" & userAccount & "_" & currentDateTime

                Dim completeFileName As String = imgFileNameWithoutExtension & fileExtension

                'save file to server
                Dim pathExists As Boolean = System.IO.Directory.Exists(path)
                If Not pathExists Then
                    System.IO.Directory.CreateDirectory(path)
                End If

                UploadPageFileUpload.SaveAs(path & completeFileName)

                ' store information to DB================================================================================================ start
                Try
                    Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
                    jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

                    Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
                    jptCommand.Connection = jptConn
                    jptCommand.CommandType = Data.CommandType.StoredProcedure
                    jptCommand.CommandText = "UPLOADPICTURE"

                    jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@name", System.Data.SqlDbType.NVarChar, 60))
                    jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@picDescription", System.Data.SqlDbType.NVarChar, 50))
                    jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@uplaodDatetime", System.Data.SqlDbType.DateTime))
                    jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@userId", System.Data.SqlDbType.Int))

                    jptCommand.Parameters("@name").Value = completeFileName
                    jptCommand.Parameters("@picDescription").Value = picState
                    jptCommand.Parameters("@uplaodDatetime").Value = ForDBDateTime
                    jptCommand.Parameters("@userId").Value = CType(userId, Integer)

                    Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

                    Try
                        jptConn.Open()
                        jptDataReader = jptCommand.ExecuteReader

                        If jptDataReader.Read() Then
                            Dim retCode As String = jptDataReader(0).ToString()
                            If retCode = "0" Then
                                ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('檔案上傳成功，導向個人頁面...');window.location='Personal.aspx';", True)
                            Else
                                Response.Write("<Script language='JavaScript'>alert('檔案存入資料庫出錯');</Script>")
                            End If
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
            Catch ex As Exception
                Response.Write("<Script language='JavaScript'>alert('檔案上傳失敗" & ex.Message & "');</Script>")
                Exit Sub
            End Try
            ' store information to DB================================================================================================ end
        Else
            Response.Write("<Script language='JavaScript'>alert('沒選到檔案');</Script>")
        End If
    End Sub

    Protected Sub BtnUploadPageCancel_Click(sender As Object, e As EventArgs) Handles BtnUploadPageCancel.Click
        Response.Redirect("~\Home.aspx")
    End Sub
End Class
