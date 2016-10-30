
Partial Class Home
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

            LoadPhotosFromDB()
        End If
    End Sub

    Private Sub LoadPhotosFromDB()
        Try
            Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
            jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

            Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
            jptCommand.Connection = jptConn
            jptCommand.CommandType = Data.CommandType.StoredProcedure
            jptCommand.CommandText = "GETPHOTOSFROMDBBYRANGE"

            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@Range", System.Data.SqlDbType.Int))
            jptCommand.Parameters("@Range").Value = 15


            Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

            Try
                jptConn.Open()
                jptDataReader = jptCommand.ExecuteReader

                While jptDataReader.Read()
                    Dim photoID As String = jptDataReader(0).ToString()
                    Dim photoFileName As String = jptDataReader(1).ToString()
                    Dim photoDescription As String = jptDataReader(2).ToString()
                    Dim photoUserID As String = jptDataReader(3).ToString()
                    Dim userName As String = jptDataReader(4).ToString()
                    Dim userHeadPicture As String = jptDataReader(5).ToString()

                    'file saved path
                    Dim userPhotoPath As String = "~/img/urspics/" + photoUserID + "/"

                    Dim userHeadPicPath As String = "~/img/guest_35_35.png"
                    If Not userHeadPicture = "" Then
                        userHeadPicPath = "~/img/hdp/" + photoUserID + "/" + userHeadPicture
                    End If

                    Dim Panel_photoBlockDiv As HtmlGenericControl = New HtmlGenericControl("div")
                    Panel_photoBlockDiv.ID = "hm_photoBlockID_" + photoID
                    Panel_photoBlockDiv.Attributes.Add("class", "block")
                    Panel_photoBlockDiv.ClientIDMode = UI.ClientIDMode.Static

                    Dim Panel_photoImage As Image = New Image
                    Panel_photoImage.CssClass = "photo"
                    Panel_photoImage.ClientIDMode = UI.ClientIDMode.Static
                    Panel_photoImage.ImageUrl = userPhotoPath + photoFileName

                    Dim Panel_userHeadPic As Image = New Image
                    Panel_userHeadPic.CssClass = "user"
                    Panel_userHeadPic.ClientIDMode = UI.ClientIDMode.Static
                    Panel_userHeadPic.ImageUrl = userHeadPicPath

                    Dim Panel_userName As Label = New Label
                    Panel_userName.CssClass = "userName"
                    Panel_userName.ClientIDMode = UI.ClientIDMode.Static
                    Panel_userName.Text = userName

                    Dim Panel_photoDescription As Label = New Label
                    Panel_photoDescription.CssClass = "photoName"
                    Panel_photoDescription.ClientIDMode = UI.ClientIDMode.Static
                    Panel_photoDescription.Text = photoDescription

                    Panel_photoBlockDiv.Controls.Add(Panel_photoImage)
                    Panel_photoBlockDiv.Controls.Add(New LiteralControl("<br />"))
                    Panel_photoBlockDiv.Controls.Add(Panel_userHeadPic)
                    Panel_photoBlockDiv.Controls.Add(Panel_userName)
                    Panel_photoBlockDiv.Controls.Add(Panel_photoDescription)

                    PlaceHolderHomePageDiaplay.Controls.Add(Panel_photoBlockDiv)
                End While
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
End Class
