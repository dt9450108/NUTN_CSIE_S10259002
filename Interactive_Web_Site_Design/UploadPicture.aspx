<%@ Page Title="" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="UploadPicture.aspx.vb" Inherits="UploadPicture" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelUploadPicturePage" runat="server" DefaultButton="BtnUploadPageCancel" CssClass="PanelUploadPicturePage">
        <div id="UploadPictureStyle" class="UploadPictureStyle">
            <table align="center">
                <tr>
                    <th colspan="2">Upload</th>
                </tr>
                <tr>
                    <td style ="text-align : center ;">
                        <asp:FileUpload ID="UploadPageFileUpload" runat="server" onchange="UploadPicturePageCheck();" ClientIDMode="Static"/>
                        <br />
                        <input id="BtnUploadPicPageReset" type="button" value="重新選擇" hidden="hidden" onclick="UploadPicturePageReset();"/>
                        <br />
                        <img id="UploadPageTempPreview" alt="未有圖片可預覽" src="" hidden="hidden" width="640px"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span>圖片說明</span>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:TextBox ID="UploadPictureDescription" runat="server" AutoCompleteType="Disabled" Columns="50" Height="70px" MaxLength="50" Rows="3" style="overflow: hidden; resize: none;" TextMode="MultiLine" Width="650px"></asp:TextBox>
                        <br />
                        <asp:CustomValidator ID="UploadPictureDescriptionValidator" runat="server" ControlToValidate="UploadPictureDescription" Display="Dynamic" EnableClientScript="False" ErrorMessage="描述不能超過50字" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" style="height: 13px; width: 188px"></asp:CustomValidator>
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:Button ID="BtnUploadPageCancel" runat="server" Text="取消" />
                        <asp:Button ID="BtnUploadPageUpload" runat="server" Text="上傳" />
                    </td>
                </tr>
            </table>
        </div>
    </asp:Panel>
</asp:Content>

