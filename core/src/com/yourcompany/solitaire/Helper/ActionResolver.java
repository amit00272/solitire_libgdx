package com.yourcompany.solitaire.Helper;

public interface ActionResolver {

    public void showAdmobBanner();
    public void HideAdmobBanner();
    public void ShowAdmobVideo();
    public boolean IsAdmobVideoAvailable();
    public void  showInterstitial();
    public void customActionRequestHandler(int whatAction);

}
