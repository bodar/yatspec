$(document).ready(function () {
    $('.package-name', this).click(function() {
       $(this).siblings("dd").toggleClass("hide");
    });
});
