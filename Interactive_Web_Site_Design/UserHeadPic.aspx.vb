
Partial Class UserHeadPic
    Inherits System.Web.UI.Page

    Protected Sub Page_Load(sender As Object, e As EventArgs) Handles Me.Load
        'if user not signin then redirect to NotSignin.aspx
        If Not Session("isLoginState") = "OK" Then
            Response.Redirect("~\NotSignin.aspx")
            Exit Sub
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

        'click img to select file and upload
        UserHeadPicPageTempPreview.Attributes.Add("onclick", "document.getElementById('" + UserHeadPicPageFileUpload.ClientID + "').click();")

        'initail head picture for user
        Try
            Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
            jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

            Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
            jptCommand.Connection = jptConn
            jptCommand.CommandType = Data.CommandType.StoredProcedure
            jptCommand.CommandText = "GETACCOUNTHEADPICTUREBYID"

            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
            jptCommand.Parameters("@ID").Value = CType(Session("jpt_id").ToString(), Integer)

            Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

            Try
                jptConn.Open()
                jptDataReader = jptCommand.ExecuteReader

                If jptDataReader.Read() Then
                    Dim readStr As String = jptDataReader(0).ToString()
                    If Not readStr = "1" Then
                        If readStr = String.Empty Then
                            UserHeadPicPageTempPreview.ImageUrl = "~/img/guset_448_448.png"
                        Else
                            Dim imgPathStr As String = "~/img/hdp/" + Session("jpt_id") + "/" + readStr
                            UserHeadPicPageTempPreview.ImageUrl = imgPathStr
                        End If
                    Else
                        Response.Write("<Script language='JavaScript'>alert('不存在的帳號');</Script>")
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
    End Sub

    Protected Sub BtnUserHeadPicPageCancel_Click(sender As Object, e As EventArgs) Handles BtnUserHeadPicPageCancel.Click
        Response.Redirect("~\Home.aspx")
    End Sub

    Protected Sub BtnUserHeadPicPageUpload_Click(sender As Object, e As EventArgs) Handles BtnUserHeadPicPageUpload.Click
        If UserHeadPicPageFileUpload.HasFile Then
            Session("jpt_memberHeadPicSelected") = "1"
        End If

        If Session("jpt_memberHeadPicSelected") = "0" Then
            ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('頭像未更新，導向個人頁面...');window.location='Personal.aspx';", True)
        Else
            ' get necessary information from user
            Dim userId As String = Session("jpt_id")
            Dim userAccount As String = Session("jpt_memberAcc")

            ' dim pic stored path to ~\
            Dim pathStr As String = "~/img/hdp/" & userId & "/"
            Dim path As String = Server.MapPath(pathStr)

            Dim completeFileName As String = ""

            If UserHeadPicPageFileUpload.HasFile Then
                Try
                    ' file extension
                    Dim fileExtension As String = System.IO.Path.GetExtension(UserHeadPicPageFileUpload.FileName).ToLower()

                    'rename file
                    Dim imgFileNameWithoutExtension As String = System.IO.Path.GetFileNameWithoutExtension(UserHeadPicPageFileUpload.FileName)

                    ' rename without extension
                    imgFileNameWithoutExtension = userAccount & "_jptDB_hdp"

                    completeFileName = imgFileNameWithoutExtension & fileExtension

                    'save file to server
                    Dim pathExists As Boolean = System.IO.Directory.Exists(path)
                    If Not pathExists Then
                        System.IO.Directory.CreateDirectory(path)
                    End If

                    'delete existed head picture
                    If (System.IO.File.Exists(path + Session("jpt_memberHeadPic"))) Then
                        System.IO.File.Delete(path + Session("jpt_memberHeadPic"))
                    End If

                    UserHeadPicPageFileUpload.SaveAs(path & completeFileName)
                Catch ex As Exception
                    Response.Write("<Script language='JavaScript'>alert('檔案上傳失敗" & ex.Message & "');</Script>")
                    Exit Sub
                End Try
            Else
                ' user clear head picture, delete file
                If (System.IO.File.Exists(path + Session("jpt_memberHeadPic"))) Then
                    System.IO.File.Delete(path + Session("jpt_memberHeadPic"))
                End If
            End If

            ' store information to DB================================================================================================ start
            Try
                Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
                jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

                Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
                jptCommand.Connection = jptConn
                jptCommand.CommandType = Data.CommandType.StoredProcedure
                jptCommand.CommandText = "UPLOADHEADPICTURE"

                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
                jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@headPicture", System.Data.SqlDbType.NVarChar, 25))

                jptCommand.Parameters("@ID").Value = CType(userId, Integer)
                jptCommand.Parameters("@headPicture").Value = completeFileName

                Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

                Try
                    jptConn.Open()
                    jptDataReader = jptCommand.ExecuteReader

                    If jptDataReader.Read() Then
                        Dim retCode As String = jptDataReader(0).ToString()
                        If retCode = "0" Then
                            Session("jpt_memberHeadPic") = completeFileName
                            Session("jpt_memberHeadPicSelected") = "0"
                            ScriptManager.RegisterStartupScript(Me, Me.GetType(), "popup", "alert('頭像更新成功，導向個人頁面...');window.location='Personal.aspx';", True)
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
            ' store information to DB================================================================================================ end
        End If
    End Sub

    Protected Sub BtnUserHeadPicPageReset_Click(sender As Object, e As EventArgs) Handles BtnUserHeadPicPageReset.Click
        Session("jpt_memberHeadPicSelected") = "1"
        ScriptManager.RegisterStartupScript(Me, Me.GetType(), "UserHeadPicPageReset", "UserHeadPicPageReset();", True)
    End Sub
End Class
