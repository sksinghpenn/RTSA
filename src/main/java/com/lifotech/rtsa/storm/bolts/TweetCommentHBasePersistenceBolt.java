package com.lifotech.rtsa.storm.bolts;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.lifotech.rtsa.storm.domain.TweetComment;

/**
 * The class is Bolt which deals with HBase to persistence store.
 * 
 * @author SK Singh
 * 
 */
public class TweetCommentHBasePersistenceBolt extends BaseRichBolt {

	private static final Logger logger = LoggerFactory.getLogger(TweetCommentHBasePersistenceBolt.class);

	private static final long serialVersionUID = 1L;

	private final String collectionName = "TweetComment";

	private final String TABLE_NAME = "RTSA";

	/*
	 * Prepares the bolt
	 * 
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.task.IBolt#prepare(java.util.Map,
	 * backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
	 */
	public void prepare(Map map, TopologyContext context, OutputCollector collector) {

	}

	/*
	 * The method declares output fields.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * backtype.storm.topology.IComponent#declareOutputFields(backtype.storm
	 * .topology.OutputFieldsDeclarer)
	 */
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("tweetCommentPersitent"));
	}

	/*
	 * The methods process each tuple.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.task.IBolt#execute(backtype.storm.tuple.Tuple)
	 */
	public void execute(Tuple tuple) {

		TweetComment tweetFeed = (TweetComment) tuple.getValueByField("tweetComment");

		String clientID = (String) tuple.getValueByField("clientID");

		logger.info("TweetCommentPersistenceBolt->tweetFeed " + tweetFeed);

		Configuration conf = HBaseConfiguration.create();

		String tableName = TABLE_NAME + clientID;
		HTable table;
		try {

			HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);

			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME + clientID));

			boolean doesTableExist = doesTableExist(hbaseAdmin, tableDescriptor);

			if (!(doesTableExist)) {
				createTable(conf, clientID);
			}

			table = new HTable(conf, tableName);

			Put put = new Put(Bytes.toBytes(tweetFeed.getTweetID()));
			put.add(Bytes.toBytes("sentiment"), Bytes.toBytes("userName"), Bytes.toBytes(tweetFeed.getUserName()));
			put.add(Bytes.toBytes("sentiment"), Bytes.toBytes("screenName"), Bytes.toBytes(tweetFeed.getScrenName()));
			put.add(Bytes.toBytes("sentiment"), Bytes.toBytes("tweetText"), Bytes.toBytes(tweetFeed.getTweetText()));
			put.add(Bytes.toBytes("sentiment"), Bytes.toBytes("tweetTime"),
					Bytes.toBytes(tweetFeed.getTweetTime().toGMTString()));
			put.add(Bytes.toBytes("sentiment"), Bytes.toBytes("sentimentIndex"),
					Bytes.toBytes(tweetFeed.getSentimenIndex()));
			table.put(put);
			table.flushCommits();
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * The method checks if the table exists.
	 * 
	 * @param admin
	 * 
	 * @param table
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */

	public boolean doesTableExist(HBaseAdmin admin, HTableDescriptor table) throws IOException {
		if (admin.tableExists(table.getTableName())) {
			return true;
		}
		return false;
	}

	/*
	 * The method creates HBase table.
	 * 
	 * @param config
	 * 
	 * @param clientID
	 * 
	 * @throws IOException
	 */
	public void createTable(Configuration config, String clientID) throws IOException {

		// Instantiating HbaseAdmin class
		HBaseAdmin admin = new HBaseAdmin(config);

		// Instantiating table descriptor class
		HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME + clientID));

		// Adding column families to table descriptor
		tableDescriptor.addFamily(new HColumnDescriptor("sentiment"));

		// Execute the table through admin
		admin.createTable(tableDescriptor);
		System.out.println(" Table created ");
	}

}
