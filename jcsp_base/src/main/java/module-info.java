module io.andreygs.jcsp_base {
    requires org.jetbrains.annotations;

    exports io.andreygs.jcsp_base.common;
    exports io.andreygs.jcsp_base.context;
    exports io.andreygs.jcsp_base.internal.context to io.andreygs.jcsp;
}