<%@ Page Title="Just Photos - My Head Picture" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="UserHeadPic.aspx.vb" Inherits="UserHeadPic" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    <style type="text/css">
        .auto-style1 {
            height: 21px;
        }
        .auto-style2 {
            height: 21px;
            width: 46%;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelUserHeadPicPage" runat="server" DefaultButton="BtnUserHeadPicPageCancel">
        <div id="uploadHeadBlock" class="uploadHeadBlock">
            <table align="center">
                <tr>
                    <th colspan="2">Upload Head Picture</th>
                </tr>
                <tr>
                    <td style ="text-align : center ;" colspan="2">
                        <asp:FileUpload ID="UserHeadPicPageFileUpload" runat="server" onchange="UserHeadPicPageCheck();" ClientIDMode="Static" style="display:none;" />
                        <asp:Image ID="UserHeadPicPageTempPreview" runat="server" ClientIDMode="Static" AlternateText="未選擇頭像圖片" ImageUrl="~/img/guset_448_448.png" Width="120px" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <asp:Button ID="BtnUserHeadPicPageReset" runat="server" Text="移除頭像" ClientIDMode="Static" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <asp:button id="BtnUserHeadPicPageUpload" runat="server" text="更新頭像" clientidmode="static" />
                    </td>
                    <td>
                        <asp:button id="BtnUserHeadPicPageCancel" runat="server" text="取消編輯" clientidmode="static" />
                    </td>
                </tr>
            </table>
        </div>
    </asp:Panel>
</asp:Content>

