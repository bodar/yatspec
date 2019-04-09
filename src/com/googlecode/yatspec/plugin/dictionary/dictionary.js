var dictionary = {
    "yatspec": "Yet Another Test Specification Library"
};

yatspec.additionalSpecificationHighlights = $.map(dictionary, function (definition, term) {
    return { pattern: term, cssClass: "term" };
});

$(document).ready(function() {
    $(".term").each(function() {
        var term = $(this).text();
        var definition = dictionary[term];
        $(this).append($("<span class='definition'>" + definition + "</span>"));
    });
});