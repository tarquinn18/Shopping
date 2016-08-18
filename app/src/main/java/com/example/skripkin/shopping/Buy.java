package com.example.skripkin.shopping;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Buy
{
    public static class info
    {
        private static ArrayList<String> lists_list;
        private static ArrayList<ArrayList<product>> contents_list;
        private static String lists_path = "";
        private static String lists_dup_path = "";
        private static String prod_path = "";
        private static String prod_dup_path = "";
        private static ArrayList<product> products;


        private static void write_xml(String str, String path, String dup_path)
        {
            String p = str;
            File file = new File(path);
            File dup_file = new File(dup_path);
            if (file.exists())
            {
                if (dup_file.exists()) dup_file.delete();
                file.renameTo(dup_file);
            }
            try
            {
                FileOutputStream os = new FileOutputStream(file);
                byte[] a = str.getBytes();
                os.write(a, 0, a.length);
                os.close();
            }
            catch (Exception e)
            {
                Log.d("", e.getMessage());
            }
        }

        private static String build_lists()
        {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer;
            try
            {
                writer = new StringWriter();
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "lists");
                for (int i = 0; i < lists_list.size(); i++)
                {
                    serializer.startTag("", "list");
                    serializer.attribute("", "name", lists_list.get(i));

                    ArrayList<product> temp = contents_list.get(i);
                    for (int j = 0; j < temp.size(); j++)
                    {
                        serializer.startTag("", "product");
                        serializer.attribute("", "name", temp.get(j).name);
                        serializer.attribute("", "measure", temp.get(j).measure);
                        serializer.attribute("", "amount", String.valueOf(temp.get(j).amount));
                        serializer.attribute("", "bought", String.valueOf(temp.get(j).bought));
                        serializer.endTag("", "product");
                    }
                    serializer.endTag("", "list");
                }
                serializer.endTag("", "lists");
                serializer.endDocument();
                writer.flush();
                writer.close();
                return writer.toString();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public static void add_list(String newList, int n)
        {
            lists_list.add(newList);
            if (n < 0)
            {
                contents_list.add(new ArrayList<product>());
                write_xml(build_lists(), lists_path, lists_dup_path);
            }
            else
            {
                ArrayList<product> arrayList = new ArrayList<>();
                ArrayList<product> selectedList = contents_list.get(n);
                for (int i = 0; i < selectedList.size(); i++)
                {
                    product prod = selectedList.get(i);
                    if (!prod.bought) arrayList.add(new product(prod.amount, prod.name, prod.measure));
                }
                contents_list.add(arrayList);
                save_state();
            }
        }

        public static void remove_list(int i)
        {
            lists_list.remove(i);
            contents_list.remove(i);
            write_xml(build_lists(), lists_path, lists_dup_path);
        }

        public static void save_state()
        {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer;
            String res = "";
            try
            {
                writer = new StringWriter();
                serializer.setOutput(writer);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "products");
                for (int i = 0; i < products.size(); i++)
                {
                    serializer.startTag("", "product");
                    serializer.attribute("", "name", products.get(i).name);
                    serializer.attribute("", "measure", products.get(i).measure);
                    serializer.endTag("", "product");
                }
                serializer.endTag("", "products");
                serializer.endDocument();
                res = writer.toString();
                writer.close();
            }
            catch (Exception e) {}
            write_xml(res, prod_path, prod_dup_path);
            write_xml(build_lists(), lists_path, lists_dup_path);
        }

        public static void init_buy_info(String path)
        {
            lists_path = path + File.separator + "lists.xml";
            lists_dup_path = path + File.separator + "dup_lists.xml";
            prod_path = path + File.separator + "prod.xml";
            prod_dup_path = path + File.separator + "dup_prod.xml";
            if (lists_list == null)
            {
                lists_list = new ArrayList<>();
                contents_list = new ArrayList<>();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                File file = new File(lists_path);
                if (!file.exists()) file = new File(lists_dup_path);
                if (file.exists())
                {
                    try
                    {
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        FileInputStream is = new FileInputStream(file);
                        Document dom = builder.parse(is);
                        Element root = dom.getDocumentElement();
                        NodeList items = root.getElementsByTagName("list");
                        for (int i = 0; i < items.getLength(); i++)
                        {
                            Node item = items.item(i);
                            lists_list.add(item.getAttributes().getNamedItem("name").getNodeValue());
                            contents_list.add(new ArrayList<product>());
                            NodeList products = item.getChildNodes();
                            for (int j = 0; j < products.getLength(); j++)
                            {
                                Node prod = products.item(j);
                                product p = new product(
                                        Integer.parseInt(prod.getAttributes().getNamedItem("amount").getNodeValue()),
                                        prod.getAttributes().getNamedItem("name").getNodeValue().toString(),
                                        prod.getAttributes().getNamedItem("measure").getNodeValue().toString());
                                p.bought = Boolean.parseBoolean(prod.getAttributes().getNamedItem("bought").getNodeValue());
                                contents_list.get(i).add(p);
                            }
                        }
                    } catch (Exception e) { throw new RuntimeException(e); };
                }
            }
            if (products == null)
            {
                products = new ArrayList<>();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                File file = new File(prod_path);
                if (!file.exists())
                {
                    file = new File(prod_dup_path);
                    if (!file.exists()) return;
                }
                try
                {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    FileInputStream is = new FileInputStream(file);
                    Document dom = builder.parse(is);
                    Element root = dom.getDocumentElement();
                    NodeList items = root.getElementsByTagName("product");
                    for (int i=0;i<items.getLength();i++)
                    {
                        Node item = items.item(i);
                        products.add(new product( 0, item.getAttributes().getNamedItem("name").getNodeValue(),
                                item.getAttributes().getNamedItem("measure").getNodeValue()));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public static ArrayList<String> get_lists_list()
        {
            return lists_list;
        }

        public static ArrayList<product> get_prod_list()
        {
            return products;
        }

        public static void add_product(product p, Context c)
        {
            for (int i = 0; i < products.size(); i++)
                if (products.get(i).name.equals(p.name))
                {
                    Toast.makeText(c, "Такой продукт уже есть в списке", Toast.LENGTH_SHORT).show();
                    return;
                }
            products.add(p);
        }

        public static ArrayList<product> get_list(int selected_list)
        {
            return contents_list.get(selected_list);
        }

        public static void add_prod_to_list(int selected_list, product p)
        {
            ArrayList<product> list = contents_list.get(selected_list);
            product temp = new product(1, p.name, p.measure);
            for (int i = 0; i < list.size(); i++)
                if (list.get(i).name.equals(p.name))
                {
                    list.get(i).amount++;
                    return;
                }
            contents_list.get(selected_list).add(temp);
        }

        public static void remove_prod(int i)
        {
            products.remove(i);
        }

        public static void remove_prod_from_list(int selected_list, int i)
        {
            contents_list.get(selected_list).remove(i);
        }

        public static void set_amount(int selected_list, int j, String s)
        {
            int am = 0;
            try
            {
                am = Integer.parseInt(s);
            }
            catch (Exception e) {}
            if (!(am > 0)) am = 1;
            contents_list.get(selected_list).get(j).amount = am;
        }
    }
}
