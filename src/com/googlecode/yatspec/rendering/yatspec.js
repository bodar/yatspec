function yatspec() {}

yatspec.Regex = function() {
    RegExp.apply(this, arguments);
}

yatspec.Regex.prototype = new RegExp();

yatspec.Regex.prototype.replace = function(str, replacer, nonMatchedReplacer) {
    nonMatchedReplacer = nonMatchedReplacer || function(value) {
        return value
    };
    var result = [];

    var position = 0;
    var match;
    while (( match = this.exec(str)) != null) {
        result.push(nonMatchedReplacer(str.substring(position, match.index)));
        result.push(replacer(match));
        position = this.lastIndex;
    }
    result.push(nonMatchedReplacer(str.substring(position)));

    return result.join("");
}

yatspec.highlight = function(element, pairs) {
    if (pairs.length == 0) {
        return;
    }
    var classes = [];
    var matchGroups = [];
    $.each(pairs, function() {
        matchGroups.push("(", this.pattern, ")", "|");
        classes.push(this.cssClass);
    });
    matchGroups.pop();
    var regex = new yatspec.Regex(matchGroups.join(""), "g");

    $(element).html(regex.replace($(element).html(), function(match) {
        var matches = match.slice(1);
        for (var i = 0; i < matches.length; i++) {
            if (matches[i]) {
                return '<span class="' + classes[i] + '">' + matches[i] + '</span>'
            }
        }
    }));
}

$(document).ready(function () {
    $('.logKey').click(function() {
        $(this).next(".logValue").toggleClass("hide");
    });

    $('.logKey').next(".logValue").toggleClass("hide");


    $('.highlight.specification').each(function() {
        yatspec.highlight(this, [
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
        yatspec.highlight(this, [
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
            yatspec.highlight(this, interestingGivens.concat([
                {pattern: '"[^"]*"', cssClass: "quote" },
                {pattern: "&lt;[^\\s&]+", cssClass: "keyword" },
                {pattern: "\\??&gt;", cssClass: "keyword" },
                {pattern: "\\s[\\w:-]+=", cssClass: "constant" }
            ]));
        })
    })

}, false);



