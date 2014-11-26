package it.unitn.LODE.itunesuMetadata.model.templates;
import it.unitn.LODE.Models.StorableAsXML;
import org.simpleframework.xml.*;

 /*
<rss version="2.0"
xmlns:content="http://purl.org/rss/1.0/modules/content/"
xmlns:wfw="http://wellformedweb.org/CommentAPI/"
xmlns:dc="http://purl.org/dc/elements/1.1/"
xmlns:atom="http://www.w3.org/2005/Atom"
xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
xmlns:slash="http://purl.org/rss/1.0/modules/slash/"
xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd"
xmlns:itunesu="http://www.itunesu.com/feed">
   <channel>
      <title>Festival dell'economia di Trento - 2010</title>
      <atom:link href="http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/indexfestecon2010-3giu.html" rel="self" type="application/rss+xml" />
      <link>http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/indexfestecon2010-3giu.html</link>
      <description>Festival dell'economia di Trento - 2010</description>
      <lastBuildDate>Mon, 29 May 2010 12:24:21 +0000</lastBuildDate>
      <language>it-it</language>
      <sy:updatePeriod>hourly</sy:updatePeriod>
      <sy:updateFrequency>1</sy:updateFrequency>
      <image>
         <title>Festival dell'economia di Trento - 2010</title>
         <url>http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/im.jpg</url>
      </image>
      <itunes:summary>Festival dell'economia di Trento 2010 in podcast.</itunes:summary>
      <itunes:subtitle></itunes:subtitle>
      <itunes:author>Festival economia</itunes:author>
      <itunes:image href="http://latemar.science.unitn.it/itunes/feeds/festivaleconomia2010/im.jpg" />
      <itunes:category text="Education" />
      <itunes:category text="Education">
         <itunes:category text="Higher Education" />
      </itunes:category>
      <itunes:explicit>no</itunes:explicit>
      <itunes:owner>
         <itunes:name>Progetto LODE Unitn</itunes:name>
         <itunes:email>marco.ronchetti@unitn.it</itunes:email>
      </itunes:owner>
      <item>
          <title>Inaugurazione</title>
          <pubDate>Mon, 17 May 2010 14:04:55 +0000</pubDate>
          <dc:creator>Marco Ronchetti</dc:creator>
          <enclosure url="http://latemar.science.unitn.it/itunes/archive/video/festivaleconomia2010/3giu/conferenza1.mp4" />
          <itunes:author>vari</itunes:author>
          <itunes:summary></itunes:summary>
          <itunes:subtitle></itunes:subtitle>
          <itunes:keywords>festival,economia,universita',trento</itunes:keywords>
          <itunes:duration></itunes:duration>
          <itunes:explicit>no</itunes:explicit>
      </item>
   </channel>
</rss>
 */

@Root(name="rss")
@NamespaceList({
@Namespace(reference="http://purl.org/rss/1.0/modules/content/", prefix ="content"),
@Namespace(reference="http://wellformedweb.org/CommentAPI/", prefix ="wfw"),
@Namespace(reference="http://purl.org/dc/elements/1.1/", prefix="dc"),
@Namespace(reference="http://www.w3.org/2005/Atom", prefix="atom"),
@Namespace(reference="http://purl.org/rss/1.0/modules/syndication/", prefix="sy"),
@Namespace(reference="http://purl.org/rss/1.0/modules/slash/", prefix="slash"),
@Namespace(reference="http://www.itunes.com/dtds/podcast-1.0.dtd", prefix="itunes"),
@Namespace(reference="http://www.itunesu.com/feed", prefix="itunesu")
})
public class CourseTemplate extends StorableAsXML {
    @Attribute(name="version")
    private String version="2.0";

    @Element(name="channel")
    private ChannelTemplateForCourse channel=null;

    public CourseTemplate(){ // needed to be a bean for XMLSerialization
       channel=new ChannelTemplateForCourse();
    }
    public ChannelTemplateForCourse getChannel(){
        return channel;
    }
    @Override
    public void update(){channel.setvars();}
}
