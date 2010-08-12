$(document).ready(function () {
    $('.logKey').click(function() {
        $(this).next(".logValue").toggleClass("hide");
    });

    $('.logKey').next(".logValue").toggleClass("hide");


    $('.highlight.specification').each(function() {
        highlight(this, [
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "Given", cssClass: "keyword" },
            {pattern: "And", cssClass: "keyword" },
            {pattern: "When", cssClass: "keyword" },
            {pattern: "Then", cssClass: "keyword" },
            {pattern: "[A-Z_]{2,}" , cssClass: "constant" },
            {pattern: "[\\d]+" , cssClass: "literal" }
        ]);
    })

    $('.highlight.results').each(function() {
        highlight(this, [
            {pattern: '"[^"]*"', cssClass: "quote" },
            {pattern: "Expected", cssClass: "keyword" },
            {pattern: "got", cssClass: "keyword" },
            {pattern: "[A-Z_]{2,}" , cssClass: "constant" },
            {pattern: "[\\d]+" , cssClass: "literal" }
        ]);
    })

    $('.example').each(function() {
        var interestingGivens = $('.interestingGiven', this).map(function() {
            return { pattern: $(this).text(), cssClass: "interestingGiven" };
        }).get();

        $('.highlight.xml', this).each(function() {
            highlight(this, interestingGivens.concat([
                {pattern: '"[^"]*"', cssClass: "quote" },
                {pattern: "&lt;[^\\s&]+", cssClass: "keyword" },
                {pattern: "\\??&gt;", cssClass: "keyword" },
                {pattern: "\\s[\\w:-]+=", cssClass: "constant" }
            ]));
        })
    })

}, false);
