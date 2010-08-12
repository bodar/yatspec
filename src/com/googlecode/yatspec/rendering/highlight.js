RegExp.prototype.replace = function(str, replacer, nonMatchedReplacer) {
    nonMatchedReplacer = nonMatchedReplacer || function(value) {return value};
    var result = [];

    var position = 0;
    var match;
    while(( match = this.exec(str)) != null) {
        result.push(nonMatchedReplacer(str.substring(position, match.index)));
        result.push(replacer(match));
        position = this.lastIndex;
    }
    result.push(nonMatchedReplacer(str.substring(position)));

    return result.join("");
}

function highlight(element, pairs) {
    if(pairs.length == 0){
        return; 
    }
    var classes = [];
    var matchGroups = [];
    $.each(pairs, function() {
        matchGroups.push("(", this.pattern, ")", "|");
        classes.push(this.cssClass);
    });
    matchGroups.pop();
    var regex = new RegExp(matchGroups.join(""), "g");

    $(element).html(regex.replace($(element).html(), function(match) {
        var matches = match.slice(1);
        for (var i = 0; i < matches.length; i++) {
            if (matches[i]) {
                return '<span class="' + classes[i] + '">' + matches[i] +'</span>'
            }
        }
    }));
}


