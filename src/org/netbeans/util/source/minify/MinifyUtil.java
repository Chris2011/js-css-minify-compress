/**
 * Copyright [2013] Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.util.source.minify;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.googlecode.htmlcompressor.compressor.XmlCompressor;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.netbeans.minify.ui.MinifyProperty;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public class MinifyUtil {

    
    private final String javascriptCompressError = "// JS Minify is not supported for this code...\n";
    
    MinifyResult minify(FileObject parentFile, MinifyProperty minifyProperty) {
        int directory = 0, cssFile = 0, jsFile = 0, htmlFile = 0, xmlFile = 0, jsonFile = 0;
        MinifyResult minifyResult = new MinifyResult();
//        InputOutput io = IOProvider.getDefault().getIO(Bundle.CTL_Minify(), false);
        for (FileObject file : parentFile.getChildren()) {
            if (file.isFolder()) {
                directory++;
                MinifyResult preMinifyResult = minify(file, minifyProperty);
                directory = directory + preMinifyResult.getDirectories();
                cssFile = cssFile + preMinifyResult.getCssFiles();
                jsFile = jsFile + preMinifyResult.getJsFiles();
                htmlFile = htmlFile + preMinifyResult.getHtmlFiles();
                xmlFile = xmlFile + preMinifyResult.getXmlFiles();
                jsonFile = jsonFile + preMinifyResult.getJsonFiles();
                minifyResult.setInputJsFilesSize(minifyResult.getInputJsFilesSize() + preMinifyResult.getInputJsFilesSize());
                minifyResult.setOutputJsFilesSize(minifyResult.getOutputJsFilesSize() + preMinifyResult.getOutputJsFilesSize());
                minifyResult.setInputCssFilesSize(minifyResult.getInputCssFilesSize() + preMinifyResult.getInputCssFilesSize());
                minifyResult.setOutputCssFilesSize(minifyResult.getOutputCssFilesSize() + preMinifyResult.getOutputCssFilesSize());
                minifyResult.setInputHtmlFilesSize(minifyResult.getInputHtmlFilesSize() + preMinifyResult.getInputHtmlFilesSize());
                minifyResult.setOutputHtmlFilesSize(minifyResult.getOutputHtmlFilesSize() + preMinifyResult.getOutputHtmlFilesSize());
                minifyResult.setInputXmlFilesSize(minifyResult.getInputXmlFilesSize() + preMinifyResult.getInputXmlFilesSize());
                minifyResult.setOutputXmlFilesSize(minifyResult.getOutputXmlFilesSize() + preMinifyResult.getOutputXmlFilesSize());
                minifyResult.setInputJsonFilesSize(minifyResult.getInputJsonFilesSize() + preMinifyResult.getInputJsonFilesSize());
                minifyResult.setOutputJsonFilesSize(minifyResult.getOutputJsonFilesSize() + preMinifyResult.getOutputJsonFilesSize());

            } else if (file.getExt().equalsIgnoreCase("js") && minifyProperty.isBuildJSMinify()) {
                jsFile++;
                try {
                    Boolean allow = true;
                    String inputFilePath = file.getPath();
                    String outputFilePath;
   
                    if (minifyProperty.isSkipPreExtensionJS() && minifyProperty.isBuildJSMinify() && minifyProperty.isNewJSFile()) {
                        if (minifyProperty.getPreExtensionJS() != null && !minifyProperty.getPreExtensionJS().trim().isEmpty()
                                && file.getName().matches(".*" + Pattern.quote(minifyProperty.getSeparatorJS() + minifyProperty.getPreExtensionJS()))) {
                            allow = false;
                        }
                    }
                    if (allow) {
                        if (minifyProperty.isNewJSFile() && minifyProperty.getPreExtensionJS() != null && !minifyProperty.getPreExtensionJS().trim().isEmpty()) {
                            outputFilePath = file.getParent().getPath() + File.separator + file.getName() + minifyProperty.getSeparatorJS() + minifyProperty.getPreExtensionJS() + "." + file.getExt();
                        } else {
                            outputFilePath = inputFilePath;
                        }

                        MinifyFileResult minifyFileResult = compress(inputFilePath,"text/javascript", outputFilePath, minifyProperty);
                        minifyResult.setInputJsFilesSize(minifyResult.getInputJsFilesSize() + minifyFileResult.getInputFileSize());
                        minifyResult.setOutputJsFilesSize(minifyResult.getOutputJsFilesSize() + minifyFileResult.getOutputFileSize());
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (file.getExt().equalsIgnoreCase("css") && minifyProperty.isBuildCSSMinify()) {
                cssFile++;
                try {
                    Boolean allow = true;
                    String inputFilePath = file.getPath();
                    String outputFilePath;

                    if (minifyProperty.isSkipPreExtensionCSS() && minifyProperty.isBuildCSSMinify() && minifyProperty.isNewCSSFile()) {
                        // String postFix = file.getName().substring(file.getName().lastIndexOf(minifyProperty.getSeparatorCSS()) + 1, file.getName().length());
                        if (minifyProperty.getPreExtensionCSS() != null && !minifyProperty.getPreExtensionCSS().trim().isEmpty()
                                && file.getName().matches(".*" + Pattern.quote(minifyProperty.getSeparatorCSS() + minifyProperty.getPreExtensionCSS()))) {
                            allow = false;
                        }
                    }
                    if (allow) {
                        if (minifyProperty.isNewCSSFile() && minifyProperty.getPreExtensionCSS() != null && !minifyProperty.getPreExtensionCSS().trim().isEmpty()) {
                            outputFilePath = file.getParent().getPath() + File.separator + file.getName() + minifyProperty.getSeparatorCSS() + minifyProperty.getPreExtensionCSS() + "." + file.getExt();
                        } else {
                            outputFilePath = inputFilePath;
                        }

                        MinifyFileResult minifyFileResult = compress(inputFilePath,"text/css", outputFilePath, minifyProperty);
                        minifyResult.setInputCssFilesSize(minifyResult.getInputCssFilesSize() + minifyFileResult.getInputFileSize());
                        minifyResult.setOutputCssFilesSize(minifyResult.getOutputCssFilesSize() + minifyFileResult.getOutputFileSize());
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (file.getExt().equalsIgnoreCase("html") && minifyProperty.isBuildHTMLMinify()) {
                htmlFile++;
                try {
                    Boolean allow = true;
                    String inputFilePath = file.getPath();
                    String outputFilePath;

                    if (minifyProperty.isSkipPreExtensionHTML() && minifyProperty.isBuildHTMLMinify() && minifyProperty.isNewHTMLFile()) {
                        // String postFix = file.getName().substring(file.getName().lastIndexOf(minifyProperty.getSeparatorHTML()) + 1, file.getName().length());
                        if (minifyProperty.getPreExtensionHTML() != null && !minifyProperty.getPreExtensionHTML().trim().isEmpty()
                                && file.getName().matches(".*" + Pattern.quote(minifyProperty.getSeparatorHTML() + minifyProperty.getPreExtensionHTML()))) {
                            allow = false;
                        }
                    }
                    if (allow) {
                        if (minifyProperty.isNewHTMLFile() && minifyProperty.getPreExtensionHTML() != null && !minifyProperty.getPreExtensionHTML().trim().isEmpty()) {
                            outputFilePath = file.getParent().getPath() + File.separator + file.getName() + minifyProperty.getSeparatorHTML() + minifyProperty.getPreExtensionHTML() + "." + file.getExt();
                        } else {
                            outputFilePath = inputFilePath;
                        }

                        MinifyFileResult minifyFileResult = compress(inputFilePath,"text/html", outputFilePath, minifyProperty);
                        minifyResult.setInputHtmlFilesSize(minifyResult.getInputHtmlFilesSize() + minifyFileResult.getInputFileSize());
                        minifyResult.setOutputHtmlFilesSize(minifyResult.getOutputHtmlFilesSize() + minifyFileResult.getOutputFileSize());
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (file.getExt().equalsIgnoreCase("xml") && minifyProperty.isBuildXMLMinify()) {
                xmlFile++;
                try {
                    Boolean allow = true;
                    String inputFilePath = file.getPath();
                    String outputFilePath;

                    if (minifyProperty.isSkipPreExtensionXML() && minifyProperty.isBuildXMLMinify() && minifyProperty.isNewXMLFile()) {
                        // String postFix = file.getName().substring(file.getName().lastIndexOf(minifyProperty.getSeparatorXML()) + 1, file.getName().length());
                        if (minifyProperty.getPreExtensionXML() != null && !minifyProperty.getPreExtensionXML().trim().isEmpty()
                                && file.getName().matches(".*" + Pattern.quote(minifyProperty.getSeparatorXML() + minifyProperty.getPreExtensionXML()))) {
                            allow = false;
                        }
                    }
                    if (allow) {
                        if (minifyProperty.isNewXMLFile() && minifyProperty.getPreExtensionXML() != null && !minifyProperty.getPreExtensionXML().trim().isEmpty()) {
                            outputFilePath = file.getParent().getPath() + File.separator + file.getName() + minifyProperty.getSeparatorXML() + minifyProperty.getPreExtensionXML() + "." + file.getExt();
                        } else {
                            outputFilePath = inputFilePath;
                        }

                        MinifyFileResult minifyFileResult = compress(inputFilePath,"text/xml-mime", outputFilePath, minifyProperty);
                        minifyResult.setInputXmlFilesSize(minifyResult.getInputXmlFilesSize() + minifyFileResult.getInputFileSize());
                        minifyResult.setOutputXmlFilesSize(minifyResult.getOutputXmlFilesSize() + minifyFileResult.getOutputFileSize());
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (file.getExt().equalsIgnoreCase("json") && minifyProperty.isBuildJSONMinify()) {
                jsonFile++;
                try {
                    Boolean allow = true;
                    String inputFilePath = file.getPath();
                    String outputFilePath;

                    if (minifyProperty.isSkipPreExtensionJSON() && minifyProperty.isBuildJSONMinify() && minifyProperty.isNewJSONFile()) {
                        // String postFix = file.getName().substring(file.getName().lastIndexOf(minifyProperty.getSeparatorJSON()) + 1, file.getName().length());
                        if (minifyProperty.getPreExtensionJSON() != null && !minifyProperty.getPreExtensionJSON().trim().isEmpty()
                                && file.getName().matches(".*" + Pattern.quote(minifyProperty.getSeparatorJSON() + minifyProperty.getPreExtensionJSON()))) {
                            allow = false;
                        }
                    }
                    if (allow) {
                        if (minifyProperty.isNewJSONFile() && minifyProperty.getPreExtensionJSON() != null && !minifyProperty.getPreExtensionJSON().trim().isEmpty()) {
                            outputFilePath = file.getParent().getPath() + File.separator + file.getName() + minifyProperty.getSeparatorJSON() + minifyProperty.getPreExtensionJSON() + "." + file.getExt();
                        } else {
                            outputFilePath = inputFilePath;
                        }

                        MinifyFileResult minifyFileResult = compress(inputFilePath,"text/x-json", outputFilePath, minifyProperty);
                        minifyResult.setInputJsonFilesSize(minifyResult.getInputJsonFilesSize() + minifyFileResult.getInputFileSize());
                        minifyResult.setOutputJsonFilesSize(minifyResult.getOutputJsonFilesSize() + minifyFileResult.getOutputFileSize());
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                //io.getOut().println("Invalid File: " + file.getPath());
            }
        }

        minifyResult.setDirectories(directory);
        minifyResult.setCssFiles(cssFile);
        minifyResult.setJsFiles(jsFile);
        minifyResult.setHtmlFiles(htmlFile);
        minifyResult.setXmlFiles(xmlFile);
        minifyResult.setJsonFiles(jsonFile);
        return minifyResult;
    }

   
    public String compressJavaScriptInternal(String oldContent, MinifyProperty minifyProperty) throws IOException {
        String content = "";
        StringWriter out = null;
        Reader in = null;
        try {
            out = new StringWriter();
            in = new StringReader(oldContent);
            JavaScriptCompressor compressor = new JavaScriptCompressor(in, new MinifyUtil.CompressorErrorReporter(oldContent, out, javascriptCompressError));
            in.close();
            in = null;
            compressor.compress(out, minifyProperty.getLineBreakPosition(), minifyProperty.isJsObfuscate(), minifyProperty.getVerbose(), minifyProperty.isPreserveSemicolon(), minifyProperty.getDisableOptimizations());
            out.flush();
            content = out.toString();            
        } 
        catch(IOException exception)
        {
            if(content.equals(""))
            {
                content = oldContent;
            }
        }
        finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            return content;
        }
    }

  
    public void compressCssInternal(Reader in, Writer out, MinifyProperty minifyProperty) throws IOException {
        try {
            CssCompressor compressor = new CssCompressor(in);
            in.close();
            in = null;
            compressor.compress(out, minifyProperty.getLineBreakPosition());
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

   
    public void compressHtmlInternal(Reader in, Writer out, MinifyProperty minifyProperty) throws IOException {
        try {
            HtmlCompressor compressor = new HtmlCompressor();
            compressor.setRemoveIntertagSpaces(true);
            compressor.setCompressCss(minifyProperty.isBuildInternalCSSMinify());               //compress inline css
            compressor.setCompressJavaScript(minifyProperty.isBuildInternalJSMinify());
            compressor.setYuiJsNoMunge(!minifyProperty.isJsObfuscate());
            String output = compressor.compress(fromStream(in));//out, minifyProperty.getLineBreakPosition());
            in.close();
            in = null;
            out.write(output);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
    
    
    
     public MinifyFileResult compressContent(String content, String mimeType, String outputFilename, MinifyProperty minifyProperty) throws IOException {
        Writer out = null;
        MinifyFileResult minifyFileResult = new MinifyFileResult();
        try {
            File outputFile = new File(outputFilename);
            out = new OutputStreamWriter(new FileOutputStream(outputFile), minifyProperty.getCharset());

            String output;
            if (mimeType.equals("text/html")) {
                HtmlCompressor compressor = new HtmlCompressor();
                compressor.setRemoveIntertagSpaces(true);
                compressor.setCompressCss(minifyProperty.isBuildInternalCSSMinify());               //compress inline css
                compressor.setCompressJavaScript(minifyProperty.isBuildInternalJSMinify());        //compress inline javascript
                compressor.setYuiJsNoMunge(!minifyProperty.isJsObfuscate());
                output = compressor.compress(content);//out, minifyProperty.getLineBreakPosition());
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderHTML())) {
                    out.write(output);
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderHTML() + "\n" + output);
                }
            } else if (mimeType.equals("text/javascript")) {
                Reader in = new StringReader(content);
                JavaScriptCompressor compressor = new JavaScriptCompressor(in, new MinifyUtil.CompressorErrorReporter(content, out, javascriptCompressError));
                in.close();
                StringWriter outputWriter = new StringWriter();
                compressor.compress(outputWriter, minifyProperty.getLineBreakPosition(), minifyProperty.isJsObfuscate(), minifyProperty.getVerbose(), minifyProperty.isPreserveSemicolon(), minifyProperty.getDisableOptimizations());
                outputWriter.flush();
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderJS())) {
                    out.write(outputWriter.toString());
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderJS() + "\n" + outputWriter.toString());
                }
                outputWriter.close();
            } else if (mimeType.equals("text/css")) {
                Reader in = new StringReader(content);
                CssCompressor compressor = new CssCompressor(in);
                in.close();
                StringWriter outputWriter = new StringWriter();
                compressor.compress(outputWriter, minifyProperty.getLineBreakPosition());
                outputWriter.flush();
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderCSS())) {
                    out.write(outputWriter.toString());
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderCSS() + "\n" + outputWriter.toString());
                }
                outputWriter.close();
            } else if (mimeType.equals("text/x-json")) {
                JSONMinifyUtil compressor = new JSONMinifyUtil();
                output = compressor.minify(content);
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderJSON())) {
                    out.write(output);
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderJSON() + "\n" + output);
                }
            } else if (mimeType.equals("text/xml-mime")) {
                XmlCompressor compressor = new XmlCompressor();
                compressor.setRemoveIntertagSpaces(true);
                compressor.setRemoveComments(true);
                compressor.setEnabled(true);
                output = compressor.compress(content);
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderXML())) {
                    out.write(output);
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderXML() + "\n" + output);
                }
            }

            out.flush();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return minifyFileResult;
    }


     
     

    public MinifyFileResult compress(String inputFilename, String mimeType, String outputFilename, MinifyProperty minifyProperty) throws IOException {
        InputStreamReader in = null;
        Writer out = null;
        MinifyFileResult minifyFileResult = new MinifyFileResult();
        try {
            File inputFile = new File(inputFilename);
            File outputFile = new File(outputFilename);
            in = new InputStreamReader(new FileInputStream(inputFile), minifyProperty.getCharset());
            minifyFileResult.setInputFileSize(inputFile.length());
            out = new OutputStreamWriter(new FileOutputStream(outputFile), minifyProperty.getCharset());
            String output;
             
            if (mimeType.equals("text/html")) {
                HtmlCompressor compressor = new HtmlCompressor();
                compressor.setRemoveIntertagSpaces(true);
                compressor.setCompressCss(minifyProperty.isBuildInternalCSSMinify());               //compress inline css
                compressor.setCompressJavaScript(minifyProperty.isBuildInternalJSMinify());        //compress inline javascript
                compressor.setYuiJsNoMunge(!minifyProperty.isJsObfuscate());
                output = compressor.compress(fromStream(in));//out, minifyProperty.getLineBreakPosition());
                out.write(MinifyProperty.getInstance().getHeaderHTML()  +"\n"+ output);
            } else if (mimeType.equals("text/javascript")) {
                Scanner scanner = new Scanner(in).useDelimiter("\\A");
                String oldContent = scanner.hasNext() ? scanner.next() : "";
                Reader oldContentReader = new StringReader(oldContent);
                JavaScriptCompressor compressor = new JavaScriptCompressor(oldContentReader, new MinifyUtil.CompressorErrorReporter(oldContent, out, javascriptCompressError));
                oldContentReader.close();
                StringWriter outputWriter = new StringWriter();
                compressor.compress(outputWriter, minifyProperty.getLineBreakPosition(), minifyProperty.isJsObfuscate(), minifyProperty.getVerbose(), minifyProperty.isPreserveSemicolon(), minifyProperty.getDisableOptimizations());
                outputWriter.flush();
                if(StringUtils.isBlank(MinifyProperty.getInstance().getHeaderJS())) {
                    out.write(outputWriter.toString());
                } else {
                    out.write(MinifyProperty.getInstance().getHeaderJS() + "\n" + outputWriter.toString());
                }
                outputWriter.close();
            } else if (mimeType.equals("text/css")) {
                CssCompressor compressor = new CssCompressor(in);
                StringWriter outputWriter = new StringWriter();
                compressor.compress(outputWriter, minifyProperty.getLineBreakPosition());
                outputWriter.flush();
                out.write(MinifyProperty.getInstance().getHeaderCSS()  +"\n"+ outputWriter.toString());
                outputWriter.close();
            } else if (mimeType.equals("text/x-json")) {
                JSONMinifyUtil compressor = new JSONMinifyUtil();
                output = compressor.minify(fromStream(in));
                out.write(MinifyProperty.getInstance().getHeaderJSON()  +"\n"+ output);
            } else if (mimeType.equals("text/xml-mime")) {
                XmlCompressor compressor = new XmlCompressor();
                compressor.setRemoveIntertagSpaces(true);
                compressor.setRemoveComments(true);
                compressor.setEnabled(true);
                output = compressor.compress(fromStream(in));
                out.write(MinifyProperty.getInstance().getHeaderXML()  +"\n"+ output);
            }
            
            in.close();
            in = null;

            out.flush();
            
            minifyFileResult.setOutputFileSize(outputFile.length());
            if (minifyProperty.isAppendLogToFile()) {
                out.append("\n<!--Size: " + minifyFileResult.getInputFileSize() + "=>"
                        + minifyFileResult.getOutputFileSize() + "Bytes "
                        + "\n Saved " + minifyFileResult.getSavedPercentage() + "%-->");
            }
            

        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return minifyFileResult;
    }

    public void compressXmlInternal(Reader in, Writer out, MinifyProperty minifyProperty) throws IOException {
        try {
            XmlCompressor compressor = new XmlCompressor();
            compressor.setRemoveIntertagSpaces(true);
            compressor.setRemoveComments(true);
            compressor.setEnabled(true);
            String output = compressor.compress(fromStream(in));//out, minifyProperty.getLineBreakPosition());
            in.close();
            in = null;
            out.write(output);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    
    public void compressJsonInternal(Reader in, Writer out, MinifyProperty minifyProperty) throws IOException {
        try {
            JSONMinifyUtil compressor = new JSONMinifyUtil();
            String output = compressor.minify(fromStream(in));
            in.close();
            in = null;
            out.write(output);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
    
    public Boolean isMinifiedFile(String fileName, String ext, String seperator)
    {
        String[] namePaths = fileName.split("\\" + seperator);
        return (namePaths.length > 1 && namePaths[namePaths.length - 1].equals(ext));
    }

    public static String fromStream(Reader in) throws IOException {
        StringBuffer srcsb = new StringBuffer();
        int c;
        while ((c = in.read()) != -1) {
            srcsb.append((char) c);
        }

        return srcsb.toString();
    }

    /* Error Reporter */
    private static final Logger logger = Logger.getLogger(MinifyUtil.class.getName());

    private static class CompressorErrorReporter implements ErrorReporter {

        private final String unMinifedContent;
        private final Writer out;
        private final String errorText;

        public CompressorErrorReporter(String unMinifedContent, Writer out, String errorText)
        {
            this.unMinifedContent = unMinifedContent;
            this.out = out;
            this.errorText = errorText;
        }
        
        @Override
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                logger.log(Level.WARNING, message);
            } else {
                logger.log(Level.WARNING, line + ':' + lineOffset + ':' + message);
            }
        }

        @Override
        public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                logger.log(Level.SEVERE, message);
            } else {
                logger.log(Level.SEVERE, line + ':' + lineOffset + ':' + message);
            }
            restoreOldContent();
        }

        @Override
        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            restoreOldContent();
            return new EvaluatorException(message);
        }
        
        private void restoreOldContent()
        {
            try
            {
                if(out != null)
                {
                    
                    out.write(errorText + unMinifedContent);
                    out.flush();
                    IOUtils.closeQuietly(out);
                }
            } catch (IOException ex)
            {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
