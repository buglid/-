export const pyCode = `import argparse
import time
import requests
import random
from bs4 import BeautifulSoup
import datetime
import pymysql

def dataToSql(headers,year,month,area_id,enter_date,user_id,remarks):
    seconds = random.randint(2, 5)
    time.sleep(seconds)

    conn = pymysql.connect(host='localhost',
                         user='root',
                         password='root',
                         database='world',
                         charset='utf8')

    cursor = conn.cursor()

    # 获取网址
    url = 'http://www.tianqihoubao.com/aqi/' + citys + '-' + str("%04d" % year) + str("%02d" % month) + '.html'
    # 发送请求
    response = requests.get(url=url, headers=headers)  # 得到响应
    # 筛选数据
    soup = BeautifulSoup(response.text, 'html.parser')
    tr = soup.find_all('tr')  # 用 BeautifulSoup 能更快速便捷地进行解析和提取,也可以用re，xpath等方法
    for k in tr[1:]:  # 从1开始是不生成列标题
        td = k.find_all('td')
        Date = td[0].get_text().strip()  # 去掉首尾换行符
        Quality_grade = td[1].get_text().strip()
        AQI = td[2].get_text().strip()
        AQI_rank = td[3].get_text().strip()
        PM2_5 = td[4].get_text().strip()
        PM10 = td[5].get_text().strip()
        SO2 = td[6].get_text().strip()
        NO2 = td[7].get_text().strip()
        CO = td[8].get_text().strip()
        O3 = td[9].get_text().strip()

        print(Date, Quality_grade, AQI, AQI_rank, PM2_5, PM10, SO2, NO2, CO, O3)

        sql = f"insert into pollution(area_id,Date,PM2_5, PM10, SO2, NO2, CO, O3,enter_date,user_id,remarks) values ({area_id},'{Date}',{PM2_5},{PM10},{SO2},{NO2},{CO},{O3},'{enter_date}',{user_id},'{remarks}')"
        print(sql)
        cursor.execute(sql)
        conn.commit()
    print("--------------------------------------")


def paqu(citys,start_time,end_time,area_id,enter_date,user_id,remarks):
    # 生成请求头，浏览器可看
    headers = {
        'Accept': '*/*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.5005.63 Safari/537.36 Edg/102.0.1245.33'
    }

    start_year = start_time.year
    start_month = start_time.month
    start_day = start_time.day
    end_year = end_time.year
    end_month = end_time.month
    end_day = end_time.day

    for month in range(start_month,13):
        dataToSql(headers,start_year,month,area_id,enter_date,user_id,remarks)
    for year in range(start_year+1, end_year):  # 近五年
        for month in range(1, 13):  # 12个月
            # 停顿几秒，避免消耗对方服务器过多资源
            dataToSql(headers,year,month,area_id,enter_date,user_id,remarks)
    for month in range(1,end_month):
        dataToSql(headers,end_year,month,area_id,enter_date,user_id,remarks)


def charToDate(data):
    """若传过来的类型不是date类型则用此函数"""

    fmt = '%Y-%m-%d'
    time_tuple = time.strptime(data, fmt)
    year, month, day = time_tuple[:3]
    a_date = datetime.date(year, month, day)

    return a_date

if __name__ == '__main__':

    # #接收数据样例
    # citys = 'lanzhou'
    # start_time = '2018-10-1'   #开始时间
    # end_time = '2020-12-1'     #结束时间
    # area_id = 3401
    # enter_date = '2023-3-16'
    # user_id = 2

    parser = argparse.ArgumentParser()
    parser.add_argument('-c', '--citys', help=u'citys')
    parser.add_argument('-s', '--start_time',help=u'start_time')
    parser.add_argument('-et', '--end_time', help=u'end_time')
    parser.add_argument('-a', '--area_id', help=u'area_id')
    parser.add_argument('-ed', '--enter_date', help=u'enter_date')
    parser.add_argument('-u', '--user_id', help=u'user_id')
    args = parser.parse_args()

    citys = args.citys
    start_time = args.start_time
    end_time = args.end_time
    enter_date = args.enter_date
    area_id = int(args.area_id)
    user_id = int(args.user_id)


    remarks = '';
    start_time = charToDate(start_time)
    end_time = charToDate(end_time)
    enter_date = charToDate(enter_date)


    paqu(citys,start_time,end_time,area_id,enter_date,user_id,remarks)`