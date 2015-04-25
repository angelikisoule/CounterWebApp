package gr.soule.mavenbook.springwebpp.parser;



import gr.soule.mavenbook.springwebpp.article.Article;
import gr.soule.mavenbook.springwebpp.article.Entry;
import gr.soule.mavenbook.springwebpp.article.Feed;
import gr.soule.mavenbook.springwebpp.controller.BaseController;

import java.io.InputStream;
import java.net.URL;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.LoggerFactory;

public class ArticleParser extends TimerTask{
	private Feed feed;
	private Entry entry;

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	
	@Override
    public void run() {
        try {
            parse();
        } catch (Exception ex) {
            logger.error("ex.getMessage()", ex);
        }
    }

	public Article parse() throws Exception {
		Article article = new Article();
		

		logger.debug("Creating XML Reader");
		// UNMARSHALLER
		JAXBContext jc = JAXBContext.newInstance(Feed.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		setFeed((Feed) unmarshaller.unmarshal(new URL("http://content.24media.gr/develop/article?article=3364587&profile=c3BvcnQyNHJvaQ==&view=generic%2Fv2%2Farticle-atom")));
//		jc = JAXBContext.newInstance(Entry.class);
//		unmarshaller = jc.createUnmarshaller();
//		setEntry((Entry) unmarshaller.unmarshal(new URL("http://content.24media.gr/develop/article?article=3364587&profile=c3BvcnQyNHJvaQ==&view=generic%2Fv2%2Farticle-atom")));

		logger.debug("Parsing XML Response");
		article.setTitle(feed.getEntry().getTitle());
		article.setSummary(feed.getEntry().getSummary());
		article.setType(feed.getEntry().getType());
		for (int i = 0; i < feed.getEntry().getLink().size(); i++) {
			if(feed.getEntry().getLink().get(i).getRel().toString().equalsIgnoreCase("alternate"))
				article.setLink(feed.getEntry().getLink().get(i).getHref());
			
			if(feed.getEntry().getLink().get(i).getRel().toString().equalsIgnoreCase("enclosure"))
				if(feed.getEntry().getLink().get(i).getComment().toString().equalsIgnoreCase("main"))
					article.setPhoto(feed.getEntry().getLink().get(i).getHref());
			
		}

		return article;
	}

	
	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public Entry getEntry() {
		return entry;
	}
	
	public void setEntry(Entry entry) {
		this.entry = entry;
	}


}

