#summary $testResult.name$

= $testResult.name$ =
$testResult.notes$

== Contents ==
$testResult.testMethods:{ testMethod |
  * [#$testMethod.name$ $testMethod.displayName$]
}$

$testResult.testMethods:{ testMethod |

=== $testMethod.name$ ===

$testMethod.notes$

$testMethod.scenarios: { scenario |

{{{
$scenario.specification$
}}}

$scenario.logs.keys:{ key |
    ==== $key.value$ ====
    $scenario.logs.(key)$
}$}$}$
