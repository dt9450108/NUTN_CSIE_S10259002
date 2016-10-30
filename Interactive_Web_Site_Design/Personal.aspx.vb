Partial Class Personal
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
                headPic.ImageUrl = "~/img/hdp/" + Session("jpt_id") + "/" + Session("jpt_memberHeadPic")
            Else
                link_masterPageHeadPic.ImageUrl = "~/img/guest_35_35.png"
                headPic.ImageUrl = "~/img/guset_448_448.png"
            End If

            name.Text = Session("jpt_memberName")
            description.Text = Session("jpt_memberDescrip")

            LoadPersonalPhotos()
        End If
    End Sub

    Private Sub LoadPersonalPhotos()
        Try
            Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
            jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

            Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
            jptCommand.Connection = jptConn
            jptCommand.CommandType = Data.CommandType.StoredProcedure
            jptCommand.CommandText = "GETPERSONALPHOTOSBYID"

            '@ID int
            '@Range int

            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@Range", System.Data.SqlDbType.Int))
            jptCommand.Parameters("@ID").Value = CType(Session("jpt_id").ToString(), Integer)
            jptCommand.Parameters("@Range").Value = 15


            Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

            Try
                jptConn.Open()
                jptDataReader = jptCommand.ExecuteReader

                'file saved path
                Dim userPhotoPath As String = "~/img/urspics/" + Session("jpt_id") + "/"

                Dim userHeadPicPath As String = "~/img/guest_35_35.png"
                If Not Session("jpt_memberHeadPic") = "" Then
                    userHeadPicPath = "~/img/hdp/" + Session("jpt_id") + "/" + Session("jpt_memberHeadPic")
                End If
                Dim userName As String = Session("jpt_memberName")

                While jptDataReader.Read()
                    Dim photoID As String = jptDataReader(0).ToString()
                    Dim photoFileName As String = jptDataReader(1).ToString()
                    Dim photoDescription As String = jptDataReader(2).ToString()

                    Dim Panel_photoBlockDiv As HtmlGenericControl = New HtmlGenericControl("div")
                    Panel_photoBlockDiv.ID = "photoBlockID_" + photoID
                    Panel_photoBlockDiv.Attributes.Add("class", "block")
                    Panel_photoBlockDiv.ClientIDMode = UI.ClientIDMode.Static

                    Dim Panel_photoDeleteBtn As ImageButton = New ImageButton
                    Panel_photoDeleteBtn.ID = "photoDelete_" + photoID
                    Panel_photoDeleteBtn.CssClass = "btnDelectPhoto"
                    Panel_photoDeleteBtn.ClientIDMode = UI.ClientIDMode.Static
                    Panel_photoDeleteBtn.ImageUrl = "~/img/icon_trash.png"
                    Panel_photoDeleteBtn.OnClientClick = "return confirm('確定要刪除這張照片嗎？')"
                    AddHandler Panel_photoDeleteBtn.Click, AddressOf Panel_photoDeleteBtn_Click

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

                    Panel_photoBlockDiv.Controls.Add(Panel_photoDeleteBtn)
                    Panel_photoBlockDiv.Controls.Add(Panel_photoImage)
                    Panel_photoBlockDiv.Controls.Add(New LiteralControl("<br />"))
                    Panel_photoBlockDiv.Controls.Add(Panel_userHeadPic)
                    Panel_photoBlockDiv.Controls.Add(Panel_userName)
                    Panel_photoBlockDiv.Controls.Add(Panel_photoDescription)

                    PlaceHolderDisplayPhoto.Controls.Add(Panel_photoBlockDiv)
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

    Private Sub Panel_photoDeleteBtn_Click(sender As Object, e As EventArgs)
        Dim deletePhotoId As Integer = CType(CType(sender, ImageButton).ID.Substring(12).ToString(), Integer)

        'delete photo data from database
        Try
            Dim jptConn As System.Data.SqlClient.SqlConnection = New System.Data.SqlClient.SqlConnection
            jptConn.ConnectionString = ConfigurationManager.ConnectionStrings("JustPhotoDBConnStr").ConnectionString.ToString()

            Dim jptCommand As System.Data.SqlClient.SqlCommand = New System.Data.SqlClient.SqlCommand
            jptCommand.Connection = jptConn
            jptCommand.CommandType = Data.CommandType.StoredProcedure
            jptCommand.CommandText = "DELETEUSERPHOTOBYID"

            jptCommand.Parameters.Add(New System.Data.SqlClient.SqlParameter("@ID", System.Data.SqlDbType.Int))
            jptCommand.Parameters("@ID").Value = deletePhotoId

            Dim jptDataReader As System.Data.SqlClient.SqlDataReader = Nothing

            Try
                jptConn.Open()
                jptDataReader = jptCommand.ExecuteReader

                If jptDataReader.Read() Then
                    'delete photo file from server
                    Dim userId As String = Session("jpt_id")
                    Dim pathStr As String = "~/img/urspics/" & userId & "/"
                    Dim path As String = Server.MapPath(pathStr)
                    Dim dltPicName As String = jptDataReader(1)

                    If (System.IO.File.Exists(path + dltPicName)) Then
                        System.IO.File.Delete(path + dltPicName)
                    End If

                    'delete div and its content
                    Dim dltDivId As String = "photoBlockID_" + jptDataReader(0).ToString()
                    ScriptManager.RegisterStartupScript(Me, Me.GetType(), "deletePhotoDivById", "deletePhotoDivById('" & dltDivId & "');", True)
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
End Class
