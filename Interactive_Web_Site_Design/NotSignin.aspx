<%@ Page Title="Just Photos - 未登入" Language="VB" MasterPageFile="~/JustPhotoMaster.master" AutoEventWireup="false" CodeFile="NotSignin.aspx.vb" Inherits="NotSignin" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <asp:Panel ID="PanelNotSigninPage" runat="server" DefaultButton="jpt_notSigninPageLogin">
        <div id="not_sign_in">
            <div id="ad_contents">
                <div id="slogan">您未登入，請登入...</div>
                <div id="ad_btnpanel">
                    <asp:Button ID="jpt_notSigninPageLogin" runat="server" Text="登入" CssClass="btn btnNotLogin"/>
                </div>
            </div>
        </div>
    </asp:Panel>
</asp:Content>

