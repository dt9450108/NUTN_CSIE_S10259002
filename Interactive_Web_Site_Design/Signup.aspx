<%@ Page Title="Just Photos - 註冊帳號" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="Signup.aspx.vb" Inherits="Signup" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    <style type="text/css">
        .auto-style1 {
            width: 80px;
        }
        .auto-style2 {
            width: 188px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelSignupPage" runat="server" DefaultButton="BtnSubmit">
        <div id="signupBlock" class="signupStyle">
            <table align="center">
                <tr>
                    <th colspan="2">Sign up</th>
                </tr>
                <tr>
                    <td class="auto-style1">
                        帳號</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="TextBoxAccount" runat="server" placeholder="數字與英文，至少六碼" MaxLength="15" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:RequiredFieldValidator ID="accountRequired" runat="server" ErrorMessage="請輸入帳號" ControlToValidate="TextBoxAccount" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" EnableClientScript="False" Display="Dynamic"></asp:RequiredFieldValidator>
					    <asp:CustomValidator ID="accountValidator" runat="server" ErrorMessage="至少輸入六碼" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" ControlToValidate="TextBoxAccount" EnableClientScript="False" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
                <tr>
                    <td class="auto-style1">
                        密碼</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="TextBoxPassword" runat="server" TextMode="Password" placeholder="輸入密碼" MaxLength="30" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:RequiredFieldValidator ID="pwRequired" runat="server" ErrorMessage="請輸入密碼" ControlToValidate="TextBoxPassword" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:RequiredFieldValidator>
                        <asp:CustomValidator ID="pwValidator" runat="server" ErrorMessage="至少六碼英數字元" ControlToValidate="TextBoxPassword" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        密碼確認</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="TextBoxPasswordConfirm" runat="server" TextMode="Password" placeholder="再輸入一次密碼" MaxLength="30" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:RequiredFieldValidator ID="pwcRequired" runat="server" ErrorMessage="請輸入密碼確認" ControlToValidate="TextBoxPasswordConfirm" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:RequiredFieldValidator>
					    <asp:CompareValidator ID="pwcCompare" runat="server" ErrorMessage="密碼不同，請重新輸入" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" ControlToCompare="TextBoxPassword" ControlToValidate="TextBoxPasswordConfirm" Display="Dynamic"></asp:CompareValidator>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        Email</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="TextBoxEmail" runat="server" placeholder="輸入信箱" MaxLength="50" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:RequiredFieldValidator ID="emailRequired" runat="server" ErrorMessage="請輸入信箱" ControlToValidate="TextBoxEmail" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:RequiredFieldValidator>
					    <asp:RegularExpressionValidator ID="emailValidator" runat="server" ErrorMessage="信箱格式錯誤" ControlToValidate="TextBoxEmail" ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:RegularExpressionValidator>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        名稱</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="TextBoxName" runat="server" placeholder="輸入姓名" MaxLength="30" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:RequiredFieldValidator ID="nameRequired" runat="server" ErrorMessage="請輸入姓名" ControlToValidate="TextBoxName" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:RequiredFieldValidator>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <asp:Button ID="BtnSubmit" runat="server" Text="註冊" Font-Names="微軟正黑體" />
                    </td>
                </tr>
            </table>
        </div>
    </asp:Panel>
</asp:Content>

