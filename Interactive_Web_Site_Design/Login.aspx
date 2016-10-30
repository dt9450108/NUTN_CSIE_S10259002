<%@ Page Title="Just Photos - 登入" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="Login.aspx.vb" Inherits="Login" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    <style type="text/css">
        .auto-style1 {
            width: 85px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelLoginPage" runat="server" DefaultButton="BtnLogin">
        <div id="loginBlock">
            <table align="center">
                <tr>
                    <th colspan="2">Login</th>
                </tr>
                <tr>
                    <td class="auto-style1">
                        帳號/信箱</td>
                    <td>
                        <asp:TextBox ID="LoginPageAccount" placeholder="帳號/信箱" runat="server" AutoCompleteType="Disabled"></asp:TextBox>
                        <br />
                        <asp:RequiredFieldValidator ID="LoginAccountEmpty" runat="server" ErrorMessage="請輸入帳號或信箱" ControlToValidate="LoginPageAccount" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" EnableClientScript="False" Display="Dynamic"></asp:RequiredFieldValidator>
                        <asp:CustomValidator ID="LoginAccountNotFound" runat="server" ErrorMessage="帳號/信箱或密碼輸入錯誤" ControlToValidate="LoginPageAccount" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" EnableClientScript="False" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
                <tr>
                    <td class="auto-style1">密碼</td>
                    <td>
                        <asp:TextBox ID="LoginPagePassword" type="password" placeholder="密碼" runat="server" ValidateRequestMode="Inherit" AutoCompleteType="Disabled"></asp:TextBox>
                        <br />
                        <asp:RequiredFieldValidator ID="LoginPasswordEmpty" runat="server" ErrorMessage="請輸入密碼" ControlToValidate="LoginPagePassword" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" EnableClientScript="False" Display="Dynamic"></asp:RequiredFieldValidator>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><asp:Button ID="BtnLogin" runat="server" Text="登入" /></td>
                </tr>
            </table>
        </div>
    </asp:Panel>
</asp:Content>

