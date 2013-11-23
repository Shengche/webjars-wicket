package com.mycompany.mvnwicketbootstrapwebjars;

import com.google.javascript.jscomp.CompilationLevel;
import com.myapp.wicket.assets.FixBootstrapStylesCssResourceReference;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.RenderJavaScriptToFooterHeaderResponseDecorator;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyCssReference;
import de.agilecoders.wicket.core.markup.html.references.BootstrapPrettifyJavaScriptReference;
import de.agilecoders.wicket.core.markup.html.references.ModernizrJavaScriptReference;
import de.agilecoders.wicket.core.request.resource.caching.version.Adler32ResourceVersion;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.extensions.javascript.GoogleClosureJavaScriptCompressor;
import de.agilecoders.wicket.extensions.javascript.YuiCssCompressor;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.html5player.Html5PlayerJavaScriptReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.jqueryui.JQueryUIJavaScriptReference;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.wicket.WicketTheme;
import de.agilecoders.wicket.themes.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.webjars.util.WicketWebjars;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.serialize.java.DeflatedJavaSerializer;
import org.apache.wicket.settings.IRequestCycleSettings;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 *
 * @see com.mycompany.mvnwicketbootstrapwebjars.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

    public WicketApplication() {
        setConfigurationType(RuntimeConfigurationType.DEPLOYMENT);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();
        configureBootstrap();
        configureResourceBundles();

        optimizeForWebPerformance();
    }
    
    private void optimizeForWebPerformance() {
        getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy(
                "-v-",
                new CachingResourceVersion(new Adler32ResourceVersion())));

        getResourceSettings().setJavaScriptCompressor(new GoogleClosureJavaScriptCompressor(CompilationLevel.SIMPLE_OPTIMIZATIONS));
        getResourceSettings().setCssCompressor(new YuiCssCompressor());

        getFrameworkSettings().setSerializer(new DeflatedJavaSerializer(getApplicationKey()));
        //getResourceSettings().setCachingStrategy(new NoOpResourceCachingStrategy());

        setHeaderResponseDecorator(new RenderJavaScriptToFooterHeaderResponseDecorator());
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
    }

    /**
     * configure all resource bundles (css and js)
     */
    private void configureResourceBundles() {
        getResourceBundles().addJavaScriptBundle(WicketApplication.class, "core.js",
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getJQueryReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketEventReference(),
                (JavaScriptResourceReference) getJavaScriptLibrarySettings().getWicketAjaxReference(),
                (JavaScriptResourceReference) ModernizrJavaScriptReference.INSTANCE);

        getResourceBundles().addJavaScriptBundle(WicketApplication.class, "bootstrap.js",
                (JavaScriptResourceReference) Bootstrap.getSettings().getJsResourceReference(),
                (JavaScriptResourceReference) BootstrapPrettifyJavaScriptReference.INSTANCE);

        getResourceBundles().addJavaScriptBundle(WicketApplication.class, "bootstrap-extensions.js",
                JQueryUIJavaScriptReference.instance(),
                Html5PlayerJavaScriptReference.instance());

        

        getResourceBundles().addCssBundle(WicketApplication.class, "bootstrap-extensions.css",
                Html5PlayerCssReference.instance(),
                OpenWebIconsCssReference.instance());

        getResourceBundles().addCssBundle(WicketApplication.class, "application.css",
                (CssResourceReference) BootstrapPrettifyCssReference.INSTANCE,
                FixBootstrapStylesCssResourceReference.INSTANCE);

    }

    /**
     * configures wicket-bootstrap and installs the settings.
     */
    private void configureBootstrap() {
        final ThemeProvider themeProvider = new BootswatchThemeProvider() {
            {
                add(new WicketTheme());
                defaultTheme("wicket");
            }
        };

        
        final BootstrapSettings settings = new BootstrapSettings();
        settings.setJsResourceFilterName("footer-container")
                .setThemeProvider(themeProvider);
        Bootstrap.install(this, settings);

        BootstrapLess.install(this);
    }
}
