<%@ Page Title="Just Photos" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="Default.aspx.vb" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <div id="ad">
        <div id="ad_contents">
            <span id="slogan">回到最原始的照片分享</span>
            <div id="ad_btnpanel">
                <asp:HyperLink ID="jpt_defaultPageSignup"  runat="server" NavigateUrl="~/Signup.aspx" CssClass="btn btnLogin">註冊</asp:HyperLink>
                <br /><br />
                <asp:HyperLink ID="jpt_defaultPageLogin" runat="server" NavigateUrl="~/Login.aspx" CssClass="btn">登入</asp:HyperLink>
            </div>
        </div>
    </div>
</asp:Content>

