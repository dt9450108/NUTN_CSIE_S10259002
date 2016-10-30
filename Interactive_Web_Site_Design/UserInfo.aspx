<%@ Page Title="Just Photos - 個人資料" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="UserInfo.aspx.vb" Inherits="UserInfo" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    <style type="text/css">
        .auto-style1 {
            width: 111px;
        }
        .auto-style2 {
            width: 188px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelUserInfoPage" runat="server">
        <div id="personalStyle" class="personalStyle">
            <table align="center">
                <tr>
                    <th colspan="2">Personal</th>
                </tr>
                <tr>
                    <td class="auto-style1">
                        帳號</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="userinfo_TBoxAccount" runat="server" MaxLength="15" Enabled="False"></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td class="auto-style1">
                        新密碼</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="userinfo_TBoxPassword" runat="server" TextMode="Password" placeholder="輸入新密碼" MaxLength="30" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:CustomValidator ID="pwValidator" runat="server" ErrorMessage="至少六碼英數字元" ControlToValidate="userinfo_TBoxPassword" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        新密碼確認</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="userinfo_TBoxPasswordConfirm" runat="server" TextMode="Password" placeholder="再輸入一次新密碼" MaxLength="30" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
					    <asp:CompareValidator ID="pwcCompare" runat="server" ErrorMessage="新密碼不同，請重新輸入" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" ControlToCompare="userinfo_TBoxPassword" ControlToValidate="userinfo_TBoxPasswordConfirm" Display="Dynamic"></asp:CompareValidator>
                        <asp:CustomValidator ID="CustomValidatorPWCEmpty" runat="server" ErrorMessage="新密碼確認未輸入" ControlToValidate="userinfo_TBoxPasswordConfirm" EnableClientScript="False" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        Email</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="userinfo_TBoxEmail" runat="server" MaxLength="50" Enabled="False"></asp:TextBox>
                    </td>
                </tr>
			    <tr>
                    <td class="auto-style1">
                        名稱</td>
                    <td class="auto-style2">
                        <asp:TextBox ID="userinfo_TBoxName" runat="server" MaxLength="30" Enabled="False"></asp:TextBox>
                    </td>
                </tr>
			    <tr>
                    <td style="font-family: 微軟正黑體" class="auto-style1">
                        描述
                        <br />
                        <br />
                        <br />
                        <br />
                    </td>
                    <td>
                        <asp:TextBox ID="userinfo_TBoxDescription" runat="server" Columns="50" Height="70px" Rows="3" TextMode="MultiLine" Width="650px" style="overflow: hidden; resize: none;" MaxLength="50" AutoCompleteType="Disabled"></asp:TextBox>
					    <br />
                        <asp:CustomValidator ID="userinfo_TBoxDescriptionValid" runat="server" ErrorMessage="描述不能超過50字" Font-Bold="True" Font-Size="X-Small" ForeColor="Red" style="height: 13px; width: 188px" ControlToValidate="userinfo_TBoxDescription" EnableClientScript="False" Display="Dynamic"></asp:CustomValidator>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <asp:Button ID="BtnUserinfoCancel" runat="server" Text="取消" Font-Names="微軟正黑體" />
                        <asp:Button ID="BtnUserinfoUpdate" runat="server" Text="更新" Font-Names="微軟正黑體" />
                    </td>
                </tr>
            </table>
        </div>
    </asp:Panel>
</asp:Content>

