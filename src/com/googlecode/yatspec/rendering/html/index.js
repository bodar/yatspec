$(document).ready(function () {
    $('.package-name').next("div").toggleClass("hide");
    $('.package-name', this).click(function() {
       $(this).next("div").toggleClass("hide");
    });
});
