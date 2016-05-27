import scrapy
import csv

class AbnItem(scrapy.Item):
    # define the fields for your item here like:
    abn = scrapy.Field()
    url = scrapy.Field()
    email = scrapy.Field()

class MySpider(scrapy.Spider):
    name = 'abnEmail'
    # allowed_domains = ['example.com']

    def start_requests(self):
        with open('abn_parsed_urls.csv', 'rbU') as csv_file:
            data = csv.reader(csv_file, delimiter=",")
            abnItems = []
            for row in data:
                abnItem = AbnItem(abn=row[0], url=row[1], email=[2])
                abnItems.append(abnItem)
                yield scrapy.Request(abnItem['url'], callback=self.parse, meta={'item' : abnItem})

        # yield scrapy.Request('http://www.example.com/1.html', self.parse)
        # yield scrapy.Request('http://www.example.com/2.html', self.parse)
        # yield scrapy.Request('http://www.example.com/3.html', self.parse)

    def parse(self, response):
        abnItem = response.meta['item']
        print response.xpath('//span[@id="ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email"]/a/text()').extract()[0]
        # for h3 in response.xpath('//h3').extract():
        #     yield MyItem(title=h3)
        #
        # for url in response.xpath('//a/@href').extract():
        #     yield scrapy.Request(url, callback=self.parse)
