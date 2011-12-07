<li class="$cssClass.(package.status)$ package">
    <h4 class="package-name">$package.name$</h4>
    <div>
        <ul>
            $package.packages:indexPackage(package=it, cssClass=cssClass)$
        </ul>
        <ul>
            $package.results:indexResult(result=it, cssClass=cssClass)$
        </ul>
    </div>
</li>
