#summary $testResult.name$

= $testResult.name$ =
$testResult.annotations$

== Contents ==
$testResult.testMethods:{ testMethod |
  * [#$testMethod.displayLinkName$ $testMethod.displayName$]
}$

$testResult.testMethods:{ testMethod |

=== $testMethod.displayName$ ===

$testResult.annotations$

$testMethod.scenarios: { scenario |

{{{
$scenario.specification$
}}}

$scenario.logs.keys:{ key |
    ==== $key.value$ ====
    $scenario.logs.(key)$
}$}$}$
