$(function(){
  //errorTip.confirm(1);
  //切换分类
  $('.aiMapboxr-conttab li').click(function(){
    var i = $(this).index();
    $(this).addClass("active").siblings().removeClass("active");
    $(".tab-content>div").eq(i).show().siblings().hide();
  });
  /*点击select*/
  $(".tab-content01>ul>li>div>span.arrowup").click(function(){
    $(this).toggleClass("arrowdown");
    $(this).parent('div').siblings('ul').toggle();
  });
  $('.widthArrow').on('click',function(){
    $('.slideDiv').toggle();
  });
  $('.slideDiv li').on('click',function(){
    $(this).parent().hide();
    var timeC = $(this).text();
    $(this).parents('.choice').find('.widthSelect').html(timeC);
  });
  /*点击每一个圆点*/
  $(".aiMapboxl ul.dot li").on('click',function(){
    if($(this).hasClass("color-grey")){
      $(this).removeClass("color-grey");
      $(this).find('span').removeClass("bg-grey")
    }else{
      $(this).addClass("color-grey");
      $(this).find('span').addClass("bg-grey")
    }
  })
  /*点击全选*/
  $(".alldot-check").on('click',function(){
    if($(this).hasClass('active')){//   取消全选
      $(this).removeClass("active");
      $(".aiMapboxl ul.dot li").each(function() {
        $(this).addClass("color-grey");
        $(this).find('span').addClass("bg-grey")
      })
    }else{//选中
      $(this).addClass("active");
      $(".aiMapboxl ul.dot li").each(function() {
        $(this).removeClass("color-grey");
        $(this).find('span').removeClass("bg-grey")
      })
    }
  })


   /*点击每一个line*/
  $(".aiMapboxl ul.line li").on('click',function(){
    if($(this).hasClass("color-grey")){
      $(this).removeClass("color-grey");
      $(this).find('span').removeClass("bg-grey")
    }else{
      $(this).addClass("color-grey");
      $(this).find('span').addClass("bg-grey")
    }
  })
  /*点击全选line*/
  $(".allline-check").on('click',function(){
    if($(this).hasClass('active')){//   取消全选
      $(this).removeClass("active");
      $(".aiMapboxl ul.line li").each(function() {
        $(this).addClass("color-grey");
        $(this).find('span').addClass("bg-grey")
      })
    }else{//选中
      $(this).addClass("active");
      $(".aiMapboxl ul.line li").each(function() {
        $(this).removeClass("color-grey");
        $(this).find('span').removeClass("bg-grey")
      })
    }
  })
})
var errorTip ={
  et:'',
  at:'',
  ct:'',
  tb:'',
  ww: $(window).width(),
  wh: $(window).height(),
  confirm: function(t,cbyes,cbno){
    if (this.ct){
      this.ct.remove();
    }
    var ctxt = '<div class="tip-bg"></div><div class="confirm-tip"><div class="confirm-tip-cont" id=""><div></div></div><button class="confirm-tip-no">取 消</button>&nbsp;&nbsp;&nbsp;&nbsp;<button class="confirm-tip-yes">确 定</button></div>';
    $('body').append(ctxt);
    this.tb = $('.tip-bg').css('height',$(document).height());
    this.ct = $('.confirm-tip');
    $('.confirm-tip-cont>div').html(t);
    this.tb.show();
    this.ct.css({top:(this.wh-this.ct.height())/2,left:(this.ww-this.ct.width())/2});
    this.ct.show();
    $('.confirm-tip-yes').off().on('click',function(){
        errorTip.tb.remove();
        errorTip.ct.hide();
        cbyes && cbyes();
    });
    $('.confirm-tip-no').off().on('click',function(){
        errorTip.tb.hide();
        errorTip.ct.hide();
        cbno && cbno();
    });
    $('body').unbind('keyup').bind('keyup',function(ev){
      var oevent=ev||event;
      if(event.keyCode==13){
        $('.confirm-tip-yes').click();
      }
    });
  },
}