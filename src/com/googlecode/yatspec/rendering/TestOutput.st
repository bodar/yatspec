<html>
<head>
    <title>$testSuite$</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>
    <script src="http://subversion.is.uk.easynet.net/svn/netstream/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/highlight.js" type="text/javascript"></script>
    <!--<script src="file:///home/dev/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/highlight.js" type="text/javascript"></script>-->
    <!--<script src="http://subversion.is.uk.easynet.net/svn/netstream/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/prettifier.js" type="text/javascript"></script>-->
    <script src="file:///home/dev/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/prettifier.js" type="text/javascript"></script>

    <link rel="stylesheet" type="text/css" href="http://teamcity.is.uk.easynet.net:3000/sns/concordion.css"/>
    <link rel="stylesheet" type="text/css" href="http://teamcity.is.uk.easynet.net:3000/sns/acceptance.css"/>
    <link rel="stylesheet" href="http://subversion.is.uk.easynet.net/svn/netstream/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/highlight.css"/>
    <!--<link rel="stylesheet" href="file:///home/dev/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/highlight.css"/>-->
    <link rel="stylesheet" href="http://subversion.is.uk.easynet.net/svn/netstream/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/prettifier.css"/>
    <!--<link rel="stylesheet" href="file:///home/dev/trunk/netstream/pacman/pacman-core/src/test/java/sky/sns/testing/prettifier.css"/>-->
</head>
<body>

<div class="article">


<h1>$testSuite$</h1>

    <h2>Contents</h2>

    <ul class="contents">
        $testMethods:{ testMethod |
            <li class='$cssClass.(testMethod.status)$'><a href="TestOutput.st#$testMethod.name$">$testMethod.name$</a></li>
        }$
    </ul>

    $testMethods:{ testMethod |

    <a id="$testMethod.name$"></a>
    <h2>$testMethod.name$</h2>


        $if(!testMethod.scenarioTable.empty)$
            <h3>Scenarios</h3>

            <table class="scenario">
                <thead>
                <tr>
                    $testMethod.scenarioTable.displayHeaders: { header | <th>$header$</th> }$
                </tr>
                </thead>
                <tbody>
                $testMethod.scenarios, testMethod.scenarioTable.rows: { scenario, row |
                    <tr>
                        $row: { value | <td class='$cssClass.(scenario.status)$'><a href="TestOutput.st#$scenario.name$">$value$</a></td> }$
                    </tr>
                 }$
                </tbody>
            </table>
        $endif$

        $testMethod.scenarios: { scenario |

            <a id="$scenario.name$"></a>
            <div class="example">
                <h2>Specification</h2>
                <pre class="highlight specification">
                    $scenario.specification;separator="\n"$</pre>

                <h2>Test results:</h2>

                <pre class='highlight results $cssClass.(scenario.status)$'>
                    $scenario.message$</pre>

                 <h3 class="logKey">Interesting Givens</h3>

                <table class="interestingGivens logValue">
                $scenario.interestingGivens.keys:{ key |
                    <tr>
                        <th class="key">$key$</th>
                        <td class="interestingGiven">$scenario.interestingGivens.(key)$</td>
                    </tr>
                }$
                </table>

                $scenario.logs.keys:{ key |
                    <h3 class="logKey">$key$</h3>
                    <pre class="logValue highlight xml" >$scenario.logs.(key)$</pre>
                }$
        </div>
        }$


    }$

</div>
</body>
</html>
