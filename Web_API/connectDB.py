# Kết nối và truy vấn đến CSDL
from flask import flash, Flask, render_template, url_for, redirect, session
from datetime import datetime
import pyodbc
import flask

try:
      # Tạo chuỗi kết nối
      conn_str = (
            "Driver={SQL Server};"
            "Server=MINHPC\\SQLEXPRESS;"
            "Database=QuanLyHieuThuoc;"
            "Trusted_Connection=yes"
      )

      # Kết nối đến csdl
      conn = pyodbc.connect(conn_str)
      app = flask.Flask(__name__)
      

      # -------------- Đăng nhập, đăng xuất -------------- #
      @app.route('/')
      def redirectLogin():
            return redirect(url_for('login'))

      # Đăng nhập
      @app.route('/login')
      def login():
            return render_template('/login/login.html')
      
      # POST: Xác thực đăng nhập
      @app.route('/login/authentication', methods = ['POST'])
      def loginAuthentication():
            # Kiểm tra tài khoản có hợp lệ không?
            cursor = conn.cursor()
            account = flask.request.form['Account']
            password = flask.request.form['Password']

            # Truy vấn SQL để kiểm tra tài khoản và lấy thông tin nhân viên
            sql_check = '''
                  SELECT tNhanVien.MaNV, tNhanVien.ChucVu
                  FROM tAccountAdmin INNER JOIN tNhanVien ON tAccountAdmin.TenTK = tNhanVien.MaNV
                  WHERE tAccountAdmin.TenTK = ? AND tAccountAdmin.Pass = ?
            '''
            data = (account, password)
            cursor.execute(sql_check, data)
            account_info = cursor.fetchone()
            
            if account_info:
                  # Lưu thông tin đăng nhập vào phiên
                  session['logged_in'] = True
                  session['MaNV'] = account_info[0]  # Lưu MaNV vào session
                  session['role'] = account_info[1]   # Lưu ChucVu của nhân viên vào session
                  return redirect(url_for('dashboard'))
            else:
                  flash('Tài khoản không hợp lệ!', 'danger')
                  return redirect(url_for('login'))

      # Đăng xuất
      @app.route('/logout')
      def logout():
            # Kiểm tra xem người dùng đã đăng nhập hay chưa, nếu có thì xóa phiên
            if 'logged_in' in session:
                  session.pop('logged_in', None)
                  session.pop('MaNV', None)
                  session.pop('role', None)
            return redirect(url_for('login'))
      # --------------------------------------- #


      # -------------- Trang chủ -------------- #
      @app.route('/dashboard')
      def dashboard():
            if 'logged_in' in session:
                  # Lấy thông tin ChucVu từ session (nếu có)
                  ten = session.get('MaNV')
                  role = session.get('role')
                  return render_template('/homePage/dashboard.html', ten=ten, role=role)
            else:
                  return redirect(url_for('login'))
            
      # Lấy ra Số liệu thống kê
      @app.route('/statistical_data')
      def get_statistical_data():
            # Tổng số KH  
            cursor = conn.cursor()
            sql_tongKH = "Select Count(MaKH) as TongKH from tKhachHang"
            cursor.execute(sql_tongKH)
            results = []
            k = ["tongKH"]
            v = cursor.fetchone()
            results.append(dict(zip(k, v)))

            # Tổng doanh thu bán
            sql_tongDT = "Select Sum(TongTien) as TongDT from tHoaDonBan"
            cursor.execute(sql_tongDT)
            k = ["tongDT"]
            v = cursor.fetchone()
            results.append(dict(zip(k, v)))

            # Tống số lượng nhập
            sql_tongSLN = "Select Sum(SLNhap) as TongSLNhap from tChiTietHDN"
            cursor.execute(sql_tongSLN)
            k = ["tongSLN"]
            v = cursor.fetchone()
            results.append(dict(zip(k, v)))

            # Tổng số lượng bán
            sql_tongSLB = "Select Sum(SLBan) as TongSLBan from tChiTietHDB"
            cursor.execute(sql_tongSLB)
            k = ["tongSLB"]
            v = cursor.fetchone()
            results.append(dict(zip(k, v)))

            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      
      # Lấy ra Top 3 nhân viên có doanh thu cao nhất trong tháng
      @app.route('/top3staffs', methods = ['GET'])
      def get_top3_staffs():
            cursor = conn.cursor()
            # Lấy tháng và năm hiện tại
            cur_month = datetime.now().month
            cur_year = datetime.now().year
            sql = "exec Top3NhanVien @thang=?, @nam=?"
            data = (cur_month, cur_year)
            cursor.execute(sql, data)     
            results = []        
            keys = [] 
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      
      # Lấy ra Top 3 hàng hóa bán chạy nhất trong tháng
      @app.route('/top3medicines', methods = ['GET'])
      def get_top3_medincines():
            cursor = conn.cursor()
            # Lấy tháng và năm hiện tại
            cur_month = datetime.now().month
            cur_year = datetime.now().year
            sql = "exec Top3Thuoc @thang=?, @nam=?"
            data = (cur_month, cur_year)
            cursor.execute(sql, data)     
            results = []        
            keys = [] 
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      
      # Lấy ra doanh thu theo từng tháng trong năm
      @app.route('/getmonthlyrevenue', methods = ['GET'])
      def get_monthly_revenue():
            cursor = conn.cursor()
            # Lấy tháng và năm hiện tại
            cur_month = datetime.now().month
            cur_year = datetime.now().year      # Lấy năm hiện tại
            sql = "exec GetMonthlyRevenue @thang=?, @nam=?"
            data = (cur_month, cur_year)
            cursor.execute(sql, data)     
            results = []        
            keys = [] 
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      # --------------------------------------- #


      # -------------- Thuốc -------------- #
      # ----- Trang thuốc ----- #
      @app.route('/products')
      def products():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('/product/products.html', ten=ten, role=role)
      
      # GET: (all)
      @app.route('/medicine/getAll', methods = ['GET'])
      def getAllMedicine():
            cursor = conn.cursor()
            sql = 'Select MaThuoc,TenThuoc,ThanhPhan,DonGiaBan,DonGiaNhap,SoLuong,MaLoai from tThuoc where isDeleted = 0'
            cursor.execute(sql)
            results = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      
      # GET: (all) có điều kiện sắp xếp
      @app.route('/medicine/getAllWithCondition', methods=['GET'])
      def getAllWithCondition():
            cursor = conn.cursor()
            sortBy = flask.request.args.get('sortBy')  # Lấy giá trị của thuộc tính cần sắp xếp

            # Sử dụng câu lệnh SQL để sắp xếp danh sách thuốc theo thuộc tính đã chọn
            sql = '''
                  SELECT MaThuoc, TenThuoc, ThanhPhan, DonGiaBan, DonGiaNhap, SoLuong, MaLoai
                  FROM tThuoc
                  WHERE isDeleted = 0
                  ORDER BY {}
                  '''.format(sortBy)
            cursor.execute(sql)
            results = []
            keys = [i[0] for i in cursor.description]
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      # ----------------------- #

      # ----- Thêm thuốc ----- #
      @app.route('/products/createProducts')
      def createProducts():
            ten = session.get('MaNV')
            role = session.get('role')
            # Lấy danh sách Mã Loại
            cursor = conn.cursor()
            sql_loai_thuoc = 'SELECT MaLoai FROM tLoaiThuoc'
            cursor.execute(sql_loai_thuoc)
            ma_loai_thuoc_list = [row[0] for row in cursor.fetchall()]
            return render_template('/product/createProducts.html', ma_loai_thuoc_list=ma_loai_thuoc_list, ten=ten, role=role)

      # POST
      @app.route('/medicine/insert', methods = ['POST'])
      def insertMedicine():
            if flask.request.method == 'POST':
                  # Tự động sinh mã thuốc
                  cursor = conn.cursor()
                  sql_MaThuocFinal = "Select top 1 MaThuoc from tThuoc order by MaThuoc desc"
                  cursor.execute(sql_MaThuocFinal)
                  result = cursor.fetchone()
                  s = result[0]
                  prefix = s[0]
                  number = int(s[1:]) + 1
                  ma =  f"{prefix}{number:04d}"

                  # Trích xuất dữ liệu từ form
                  ten = flask.request.form['TenThuoc']
                  thanhphan = flask.request.form['ThanhPhan']
                  ngaysx = flask.request.form['NgaySX']
                  ngayhh = flask.request.form['NgayHH']
                  maloai = flask.request.form['MaLoai']
                  dongiaban = flask.request.form['DonGiaBan']
                  dongianhap = flask.request.form['DonGiaNhap']
                  soluong = flask.request.form['SoLuong']
                  trongluong = flask.request.form['TrongLuong']
                  anh = flask.request.form['Anh']
                  xuatxu = flask.request.form['XuatXu']
                  mota = flask.request.form['MoTa']
                  
                  # Chuyển đổi chuỗi thành datetime
                  if ngaysx != '' and ngayhh != '':
                        ngaysx = datetime.strptime(ngaysx, '%Y-%m-%dT%H:%M')
                        ngayhh = datetime.strptime(ngayhh, '%Y-%m-%dT%H:%M')

                  # Kiểm gtra thông tin nhập vào có rỗng ko?
                  if ma == '':
                        return redirect(url_for('createProducts'))

                  # Kiểm tra xem MaThuoc đã tồn tại trong bảng tThuoc hay không
                  cursor.execute('SELECT * FROM tThuoc WHERE MaThuoc = ?', (ma))
                  existing_row = cursor.fetchone()
                  if existing_row:
                        # Nếu MaThuoc đã tồn tại, thông báo rằng đã có rồi
                        res = flask.jsonify({"mess": "That Bai"})
                        res.status_code = 400
                        return redirect(url_for('createProducts'))
                  else:
                        sql = 'INSERT INTO tThuoc (MaThuoc, TenThuoc, ThanhPhan, NgaySX, NgayHH, MaLoai, DonGiaBan, DonGiaNhap, SoLuong, TrongLuong, Anh, XuatXu, MoTa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'
                        data = (ma, ten, thanhphan, ngaysx, ngayhh, maloai, dongiaban, dongianhap, soluong, trongluong, anh, xuatxu, mota)
                        cursor.execute(sql, data)
                        cursor.commit()
                        res = flask.jsonify({"mess":"Thanh Cong"})
                        res.status_code = 200
                        return redirect(url_for('products'))
      # ----------------------- #

      # ----- Chi tiết thuốc ----- #
      @app.route('/products/detail/<id>')
      def detailProducts(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('product/detailProduct.html', id=id, ten=ten, role=role)
      
      # GET by ID
      @app.route('/products/getbyid/<id>', methods = ['GET'])
      def getMedicineById(id):
            cursor = conn.cursor()
            sql = 'Select * from tThuoc where MaThuoc = ?'
            data = id
            cursor.execute(sql, data)
            result = {}
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  result = dict(zip(keys,i))
            resp = flask.jsonify(result)
            resp.status_code = 200
            return resp
      # -------------------------- #

      # ----- Sửa thuốc ----- #
      @app.route('/products/edit/<id>')
      def editProducts(id):
            ten = session.get('MaNV')
            role = session.get('role')
            # Truy vấn dữ liệu từ bảng tLoaiThuoc để lấy danh sách Mã Loại
            cursor = conn.cursor()
            sql_loai_thuoc = 'SELECT MaLoai FROM tLoaiThuoc'
            cursor.execute(sql_loai_thuoc)
            ma_loai_thuoc_list = [row[0] for row in cursor.fetchall()]

            sql = 'SELECT * FROM tThuoc WHERE MaThuoc = ?'
            cursor.execute(sql, id)
            result = cursor.fetchone()    
            result[3] = datetime.strptime(result[3], '%Y-%m-%d')
            result[4] = datetime.strptime(result[4], '%Y-%m-%d')
            return render_template('/product/editProduct.html', thuoc=result, ma_loai_thuoc_list=ma_loai_thuoc_list, id=id, ten=ten, role=role)

      # PUT by ID
      @app.route('/products/update/<id>', methods = ['POST'])
      def editMedicine(id):
            # Trích xuất dữ liệu từ request
            ten = flask.request.form['TenThuoc']
            thanhphan = flask.request.form['ThanhPhan']
            ngaysx = flask.request.form['NgaySX']
            ngayhh = flask.request.form['NgayHH']
            maloai = flask.request.form['MaLoai']
            dongiaban = flask.request.form['DonGiaBan']
            dongianhap = flask.request.form['DonGiaNhap']
            soluong = flask.request.form['SoLuong']
            trongluong = flask.request.form['TrongLuong']
            xuatxu = flask.request.form['XuatXu']
            mota = flask.request.form['MoTa']
            anh = flask.request.form['Anh']
            
            # Chuyển đổi chuỗi thành datetime
            ngaysx = datetime.strptime(ngaysx, '%Y-%m-%dT%H:%M')
            ngayhh = datetime.strptime(ngayhh, '%Y-%m-%dT%H:%M')

            # Thực hiện truy vấn SQL để cập nhật thuộc tính của loại thuốc
            cursor = conn.cursor()
            sql = 'UPDATE tThuoc SET TenThuoc=?, ThanhPhan=?, NgaySX = ?, NgayHH = ?, MaLoai=?, DonGiaBan=?, DonGiaNhap=?, SoLuong=?, TrongLuong=?, XuatXu = ?, MoTa=?, Anh=? WHERE MaThuoc=?'
            data = (ten, thanhphan, ngaysx, ngayhh, maloai, dongiaban, dongianhap, soluong, trongluong, xuatxu, mota, anh, id)
            cursor.execute(sql, data)
            conn.commit()
            
            # Trả về thông báo thành công
            res = flask.jsonify({"mess": "Thanh Cong"})
            res.status_code = 200
            return redirect(url_for('products'))
      # -------------------------- #

      # ----- Xóa thuốc ----- #
      @app.route('/products/delete/<id>')
      def deleteProducts(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('/product/deleteProduct.html', id=id, ten=ten, role=role)
      
      # Delete by ID (Chuyển thuộc tính 'isDeleted = true')
      @app.route('/products/deletebyid/<id>', methods = ['POST'])
      def deleteMedicineById(id):
            cursor = conn.cursor()
            sql = 'Update tThuoc set isDeleted = 1 where MaThuoc = ?'
            data = id
            cursor.execute(sql, data)
            conn.commit()
            
            # Trả về thông báo thành công
            res = flask.jsonify({"mess": "Thanh Cong"})
            res.status_code = 200
            return redirect(url_for('products'))
      # -------------------------- #
      # ----------------------------------- #


      # -------------- Khách hàng -------------- #
      # ----- Trang khách hàng ----- #
      @app.route('/customers')
      def renderCustomers():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('customer/customers.html', ten=ten, role=role)

      # GET (All):
      @app.get('/customers/getAll')
      def getAllCustomers():
            cursor = conn.cursor()
            sql = 'Select MaKH, TenKH, GioiTinh, NgaySinh, DiaChi, SDT from tKhachHang'
            cursor.execute(sql)
            response = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response.append(dict(zip(keys, i)))
            response = flask.jsonify(response)
            response.status = 200
            return response
      # ---------------------------- #

      # Tìm kiếm Khách theo tên  
      @app.route('/customers/getKhByTenKH/<TenKH>', methods=['GET'])
      def getKhByTenKH(TenKH='A'):
            cursor = conn.cursor()
            sql = "SELECT * FROM tKhachHang WHERE TenKH LIKE ?"
            data = '%' + TenKH + '%'
            cursor.execute(sql, data)
            results = []
            keys = [i[0] for i in cursor.description]
            for row in cursor.fetchall():
                  results.append(dict(zip(keys, row)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      # ---------------------------------------- #


      # -------------- Hóa đơn nhập -------------- #
      # ----- Trang hóa đơn nhập ----- #
      # GET (All):
      @app.route('/invoices')
      def InvoicesPage():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/invoices.html', ten=ten, role=role)

      @app.get('/invoices/getAll')
      def getAllInvoices():
            cursor = conn.cursor()
            sql = 'Select SoHDN, MaNV, NgayLap, MaNCC, TongTien from tHoaDonNhap'
            cursor.execute(sql)
            response = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response.append(dict(zip(keys, i)))
            response = flask.jsonify(response)
            response.status = 200
            return response
      # ------------------------------ #

      # ----- Chi tiết hóa đơn nhập ----- #
      # GET By ID:
      @app.route('/invoice/<id>')
      def getInvoiceDetail(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/invoiceDetail.html', id=id, ten=ten, role=role)

      @app.get('/invoices/getById/<id>')
      def getInvoiceById(id):
            cursor = conn.cursor()
            sql = 'select SoHDN, tHoaDonNhap.MaNV, tHoaDonNhap.MaNCC, NgayLap, TongTien, TenNV, TenNCC from tHoaDonNhap inner join tNhaCungCap on tHoaDonNhap.MaNCC = tNhaCungCap.MaNCC inner join tNhanVien on tHoaDonNhap.MaNV = tNhanVien.MaNV where SoHDN = ?'
            data = (id)
            cursor.execute(sql, data)
            response = {}
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response = dict(zip(keys, i))
            response = flask.jsonify(response)
            return response
      
      @app.get('/invoices/getByIDDetail/<id>')
      def getInvoiceByIDDetail(id):
            cursor = conn.cursor()
            sql = 'select * from tChiTietHDN where SoHDN = ?'
            data = (id)
            cursor.execute(sql, data)
            response = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response.append(dict(zip(keys, i)))
            response = flask.jsonify(response)
            return response
      # --------------------------------- #

      # ----- Thêm hóa đơn nhập ----- #
      # POST:
      @app.route('/invoices/createInvoice')
      def createInvoicePage():
            ten = session.get('MaNV')
            role = session.get('role')
            # Tự động tạo SoHDN theo ngày giờ
            soHDN = "HDN_" + datetime.now().strftime("%Y%m%d%H%M%S")
            date = datetime.now().strftime("%Y-%m-%d")
            # Lấy danh sách Mã Nhà CC
            cursor = conn.cursor()
            sql_nhacc = 'SELECT MaNCC FROM tNhaCungCap'
            cursor.execute(sql_nhacc)
            nhaCC_list = [row[0] for row in cursor.fetchall()]
            return render_template('invoice/createInvoice.html', ten=ten, role=role, soHDN=soHDN, date=date, nhaCC_list=nhaCC_list)

      @app.post('/invoices/postInvoice')
      def insertInvoice():
            try:
                  SoHDN = flask.request.json.get("SoHDN")
                  MaNV = flask.request.json.get("MaNV")
                  NgayLap = flask.request.json.get("NgayLap")
                  MaNCC = flask.request.json.get("MaNCC")

                  if SoHDN == None or MaNV == None or NgayLap == None or MaNCC == None:
                        response = flask.jsonify({"msg": "Nhập đủ thông tin"})
                        return redirect('/invoices/postInvoice', Response=response)

                  cursor = conn.cursor()
                  sql = "insert into tHoaDonNhap (SoHDN, MaNV, NgayLap, MaNCC, TongTien) values (?, ?, ?, ?, 0)"
                  
                  values = (SoHDN, MaNV, NgayLap, MaNCC)
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Thêm hóa đơn nhập thành công!"}) 
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # ----------------------------- #

      # ----- Sửa hóa đơn nhập ----- #
      @app.get('/invoices/updateInvoicePage/<id>')
      def updateInvoicePage(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/updateInvoicePage.html', id=id, ten=ten, role=role)

      # PUT:
      @app.put('/invoices/putInvoice/<id>')
      def updateInvoice(id):
            try:
                  MaNV = flask.request.json.get("MaNV")
                  NgayLap = flask.request.json.get("NgayLap")
                  MaNCC = flask.request.json.get("MaNCC")
                  TongTien = flask.request.json.get("TongTien")

                  cursor = conn.cursor()
                  sql = 'update tHoaDonNhap set MaNV = ?, NgayLap = ?, MaNCC = ?, TongTien = ? where SoHDN = ?'
                  
                  values = (MaNV, NgayLap, MaNCC, TongTien, str(id))
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Sửa hóa đơn nhập thành công!"}) 
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # ---------------------------- #

      # ----- Xóa hóa đơn nhập ----- #
      @app.route('/invoices/deleteInvoices/<id>')
      def deleteInvoicePage(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/deleteInvoice.html', id=id, ten=ten, role=role)

      # DELETE:
      @app.delete('/invoices/deleteInvoice/<id>')
      def deleteInvoice(id):
            try:
                  cursor = conn.cursor()
                  sql1 = "delete from tChiTietHDN where SoHDN = ?"
                  sql2 = "delete from tHoaDonNhap where SoHDN = ?"
                  values = (id)
                  cursor.execute(sql1, values)
                  cursor.execute(sql2, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Xóa hóa đơn nhập thành công!"}) 
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # ---------------------------- #

      # ----- Lấy thông tin chi tiết hóa đơn nhập ----- #
      @app.get('/invoices/getInvoiceDetail/<idInvoice>/<idProduct>')
      def getInvoiceDetailWithID(idInvoice, idProduct):
            cursor = conn.cursor()
            sql = 'select * from tChiTietHDN where SoHDN = ? and MaThuoc = ?'
            data = (idInvoice, idProduct)
            cursor.execute(sql, data)
            response = {}
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response = dict(zip(keys, i))
            response = flask.jsonify(response)
            return response
      # ----------------------------------------------- #

      # ----- Thêm chi tiết hóa đơn nhập ----- #
      @app.route('/invoices/createInvoiceDetail/<id>')
      def createInvoiceDetailPage(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/createInvoiceDetailPage.html', id=id, ten=ten, role=role)

      @app.post('/invoices/postInvoiceDetail/<id>')
      def createInvoiceDetail(id):
            try:
                  MaThuoc = flask.request.json.get("MaThuoc")
                  SLNhap = flask.request.json.get("SLNhap")
                  ThanhTien = flask.request.json.get("ThanhTien")

                  cursor = conn.cursor()
                  sql = "insert into tChiTietHDN (SoHDN, MaThuoc, SLNhap, ThanhTien) values (?, ?, ?, ?)"
                  
                  values = (id, MaThuoc, SLNhap, ThanhTien)
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Thêm chi tiết hóa đơn thành công!"}) 
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # -------------------------------------- #

      # ----- Sửa chi tiết hóa đơn nhập ----- #
      @app.route('/invoices/updateInvoiceDetailPage/<idInvoice>/<idProduct>')
      def updateInvoiceDetailPage(idInvoice, idProduct):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/updateInvoiceDetailPage.html', idInvoice=idInvoice, idProduct=idProduct, ten=ten, role=role)
      
      @app.put('/invoices/putDetailInvoice/<idInvoice>/<idProduct>')
      def putInvoiceDetail(idInvoice, idProduct):
            SLNhap = flask.request.json.get('SLNhap')
            ThanhTien = flask.request.json.get('ThanhTien')
            cursor = conn.cursor()
            sql = 'update tChiTietHDN set SLNhap = ?, ThanhTien = ? where SoHDN = ? and MaThuoc = ?'
            data = (SLNhap, ThanhTien, idInvoice, idProduct)
            cursor.execute(sql, data)
            cursor.commit()
            response = flask.jsonify({"msg": "Sửa chi tiết hóa đơn thành công!"})
            return response
      # ------------------------------------- #
      
      # ----- Xóa chi tiết hóa đơn nhập ----- #
      @app.route('/invoices/deleteInvoiceDetailPage/<idInvoice>/<idProduct>')
      def deleteInvoiceDetailPage(idInvoice, idProduct):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('invoice/deleteInvoiceDetailPage.html', idInvoice=idInvoice, idProduct=idProduct, ten=ten, role=role)
      
      @app.delete('/invoices/deleteInvoiceDetail/<idInvoice>/<idProduct>')
      def deleteInvoiceDetail(idInvoice, idProduct):
            cursor = conn.cursor()
            sql = 'delete from tChiTietHDN where SoHDN = ? and MaThuoc = ?'
            data = (idInvoice, idProduct)
            cursor.execute(sql, data)
            cursor.commit()
            response = flask.jsonify({"msg": "Xóa chi tiết hóa đơn thành công!"})
            return response
      # ------------------------------------- #
      # ------------------------------------------ #


      # -------------- Hóa đơn bán -------------- #
      # ----- Trang hóa đơn bán ----- #
      @app.route('/bills')
      def bills():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('/billOfSale/bills.html', ten=ten, role=role)
      
      # GET: (all)
      @app.route('/bill/getAll', methods = ['GET'])
      def getAllBill():
            cursor = conn.cursor()
            sql = 'Select SoHDB,MaNV,NgayLap,MaKH,TongTien from tHoaDonBan where MaNV is not null'
            cursor.execute(sql)
            results = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      
      # GET: (all) có điều kiện sắp xếp
      @app.route('/bills/getAllWithCondition', methods=['GET'])
      def getAllBillWithCondition():
            cursor = conn.cursor()
            sortBy = flask.request.args.get('sortBy')  # Lấy giá trị của thuộc tính cần sắp xếp

            # Sử dụng câu lệnh SQL để sắp xếp danh sách hóa đơn bán theo thuộc tính đã chọn
            sql = '''
                  SELECT *
                  FROM tHoaDonBan
                  ORDER BY {} desc
                  '''.format(sortBy)
            cursor.execute(sql)
            results = []
            keys = [i[0] for i in cursor.description]
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      # ----------------------------- #

      # ----- Chi tiết hóa đơn bán ----- #
      @app.route('/bills/detail/<id>')
      def detailBills(id):
            ten = session.get('MaNV')
            role = session.get('role')
            # Lấy ra thông tin hóa đơn bán
            cursor = conn.cursor()
            sql_hdb = '''
                  Select SoHDB,nv.MaNV,TenNV,NgayLap,kh.MaKH,TenKH,TongTien
                  from tHoaDonBan hdb inner join tNhanVien nv on hdb.MaNV = nv.MaNV
					        inner join tKhachHang kh on hdb.MaKH = kh.MaKH
                  where SoHDB = ?   '''
            cursor.execute(sql_hdb, id)
            result = cursor.fetchone()  
            return render_template('/billOfSale/detailBill.html', id=id, infoBill=result, ten=ten, role=role)
      
      # GET by ID
      @app.route('/bills/getDetailbyID/<id>', methods = ['GET'])
      def getDetailById(id):
            cursor = conn.cursor()
            sql = 'Select * from tChiTietHDB where SoHDB = ?'
            cursor.execute(sql, id)
            result = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  result.append(dict(zip(keys,i)))
            resp = flask.jsonify(result)
            resp.status_code = 200
            return resp
      # -------------------------- #
      # ----------------------------------------- #


      # -------------- Đơn hàng -------------- #
      # ----- Trang đơn hàng ----- #
      @app.route('/orders')
      def orders():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('/order/orders.html', ten=ten, role=role)
      
      # GET: (all)
      @app.route('/orders/getAll', methods = ['GET'])
      def getAllOrder():
            cursor = conn.cursor()
            today = datetime.now().date()
            sql = 'Select SoHDB,NgayLap,MaKH,TongTien from tHoaDonBan where NgayLap = ? and MaNV is null'
            data = str(today)
            cursor.execute(sql, data)
            results = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      # -------------------------- #

      # ----- Chi tiết đơn hàng ----- #
      @app.route('/orders/detail/<id>')
      def detailOrders(id):
            ten = session.get('MaNV')
            role = session.get('role')
            # Lấy ra thông tin đơn hàng
            cursor = conn.cursor()
            sql_hdb = 'SELECT SoHDB,NgayLap,MaKH,TongTien from tHoaDonBan where SoHDB = ?'
            cursor.execute(sql_hdb, id)
            result = cursor.fetchone()  
            return render_template('/order/detailOrder.html', id=id, infoOrder=result, ten=ten, role=role)
      
      # GET by ID
      @app.route('/orders/getDetailbyID/<id>', methods = ['GET'])
      def getDetailOrderById(id):
            cursor = conn.cursor()
            sql = 'Select * from tChiTietHDB where SoHDB = ?'
            cursor.execute(sql, id)
            result = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  result.append(dict(zip(keys,i)))
            resp = flask.jsonify(result)
            resp.status_code = 200
            return resp
      # ----------------------------- #

      # ----- Cập nhật đơn hàng ----- #
      @app.route('/orders/updateOrder/<id>', methods=['PUT'])
      def updateOrder(id):
            cursor = conn.cursor()
            # Lấy MaNV từ dữ liệu gửi lên
            data = flask.request.json
            ma_nv = data.get('MaNV')

            # Thực hiện cập nhật đơn hàng với mã nhân viên
            sql_update = 'UPDATE tHoaDonBan SET MaNV = ? WHERE SoHDB = ?'
            cursor.execute(sql_update, (ma_nv, id))
            conn.commit()

            # Trả về thông báo cho client
            resp = flask.jsonify({'msg': 'Xác nhận đơn hàng thành công!'})
            resp.status_code = 200
            return resp
      # ----------------------------- #
      # -------------------------------------- #
      

      # -------------- Nhà cung cấp -------------- #
      # ----- Trang nhà cung cấp ----- #
      @app.route('/suppliers')
      def getAllSuppliersPage():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('supplier/suppliers.html', ten=ten, role=role)

      # GET (All):
      @app.get('/suppliers/getAll')
      def getALlSuppliers():
            cursor = conn.cursor()
            sql = "select * from tNhaCungCap where isDeleted is null or isDeleted = 0"
            cursor.execute(sql)
            response = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response.append(dict(zip(keys, i)))
            response = flask.jsonify(response)
            return response
      # ------------------------------ #

      # Tìm kiếm nhà cung cấp theo tên  
      @app.route('/suppliers/getNccByTenNCC/<TenNCC>', methods=['GET'])
      def getNccByTenNCC(TenNCC='A'):
            cursor = conn.cursor()
            sql = "SELECT * FROM tNhaCungCap WHERE TenNCC LIKE ? and isDeleted = 0"
            data = '%' + TenNCC + '%'
            cursor.execute(sql, data)
            results = []
            keys = [i[0] for i in cursor.description]
            for row in cursor.fetchall():
                  results.append(dict(zip(keys, row)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp

      # ----- Thêm nhà cung cấp ----- #
      # POST
      @app.route('/suppliers/createSupplier')
      def createSupplier():
            ten = session.get('MaNV')
            role = session.get('role')
            # Tự động sinh mã Nhà CC
            cursor = conn.cursor()
            sql_MaNCCFinal = "Select top 1 MaNCC from tNhaCungCap order by MaNCC desc"
            cursor.execute(sql_MaNCCFinal)
            result = cursor.fetchone()
            s = result[0]
            prefix = s[0:3]
            number = int(s[3:]) + 1
            ma =  f"{prefix}{number:02d}"
            return render_template('supplier/createSupplier.html', maNCC=ma, ten=ten, role=role)

      @app.post('/suppliers/postSupplier')
      def postSupplier():
            try:
                  MaNCC = flask.request.json.get("MaNCC")
                  TenNCC = flask.request.json.get("TenNCC")
                  SoDT = flask.request.json.get("SDT")
                  Email = flask.request.json.get("Email")
                  DiaChi = flask.request.json.get("DiaChi")

                  cursor = conn.cursor()
                  sql = "insert into tNhaCungCap values (?, ?, ?, ?, ?, ?)"
                  
                  values = (MaNCC, TenNCC, SoDT, Email, DiaChi, 0)
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Thêm nhà cung cấp thành công!"}) 
                  return response
            except Exception as ex:
                  return flask.jsonify({"msg": str(ex)})
      # ----------------------------- #

      # ----- Lấy nhà cung cấp ----- #
      # GET By ID:
      @app.get('/suppliers/getByID/<id>')
      def getSupplierByID(id):
            cursor = conn.cursor()
            sql = "select * from tNhaCungCap where MaNCC = ?"
            data = (id)
            cursor.execute(sql, data)
            response = {}
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  response = dict(zip(keys, i))
            response = flask.jsonify(response)
            return response
      # ---------------------------- #

      # ----- Sửa nhà cung cấp ----- #
      @app.route('/suppliers/updateSupplier/<id>')
      def updateSupplierPage(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('supplier/updateSupplier.html', id=id, ten=ten, role=role)

      # PUT:
      @app.put('/suppliers/putSupplier/<id>')
      def putSupplier(id):
            try:
                  TenNCC = flask.request.json.get("TenNCC")
                  SoDT = flask.request.json.get("SDT")
                  Email = flask.request.json.get("Email")
                  DiaChi = flask.request.json.get("DiaChi")

                  cursor = conn.cursor()
                  sql = 'update tNhaCungCap set TenNCC = ?, SDT = ?, Email = ?, DiaChi = ? where MaNCC = ?'
                  
                  values = (TenNCC, SoDT, Email, DiaChi, str(id))
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Cập nhật nhà cung cấp thành công!"}) 
                  response.status = 200
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # ---------------------------- #

      # ----- Xóa nhà cung cấp ----- #
      @app.route('/suppliers/deleteSupplier/<id>')
      def deleteSupplierPage(id):
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('supplier/deleteSupplier.html', id=id, ten=ten, role=role)

      # DELETE:
      @app.delete('/suppliers/removeSupplier/<id>')
      def deleteSupplier(id):
            try:
                  cursor = conn.cursor()
                  sql = 'update tNhaCungCap set isDeleted = 1 where MaNCC = ?'
                  
                  values = (str(id))
                  cursor.execute(sql, values)
                  cursor.commit()

                  response = flask.jsonify({"msg": "Xóa nhà cung cấp thành công!"}) 
                  response.status = 200
                  return response
            except Exception as e:
                  return flask.jsonify({"msg": str(e)})
      # ---------------------------- #
      # ------------------------------------------ #


      # -------------- Nhân viên -------------- #
      # ----- Trang nhân viên ----- #
      @app.route('/staffs')
      def staffs():
            ten = session.get('MaNV')
            role = session.get('role')
            return render_template('/staff/staffs.html', ten=ten, role=role)
      
      # GET: (all)
      @app.route('/staffs/getAll', methods = ['GET'])
      def getAllStaff():
            cursor = conn.cursor()
            sql = 'Select * from tNhanVien where isDeleted = 0 or isDeleted is null'
            cursor.execute(sql)
            results = []
            keys = []
            for i in cursor.description:
                  keys.append(i[0])
            for i in cursor.fetchall():
                  results.append(dict(zip(keys,i)))
            resp = flask.jsonify(results)
            resp.status_code = 200
            return resp
      # ----------------------------- #

      # Tìm kiếm Nhân viên theo tên  
      @app.route('/staffs/getNvByTenNV/<TenNV>', methods=['GET'])
      def getNvByTenNV(TenNV='A'):
            cursor = conn.cursor()
            sql = "SELECT * FROM tNhanVien WHERE TenNV LIKE ? and isDeleted = 0"
            data = '%' + TenNV + '%'
            cursor.execute(sql, data)
            results = []
            keys = [i[0] for i in cursor.description]
            for row in cursor.fetchall():
                  results.append(dict(zip(keys, row)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp

      # ----- Thêm nhân viên ----- #
      @app.route('/staffs/createStaff')
      def createStaff():
            ten = session.get('MaNV')
            role = session.get('role')
            # Lấy danh sách Chức vụ
            list_chuc_vu = ['Quản lý', 'Bán hàng', 'Thủ kho']
            return render_template('/staff/createStaff.html', list_chuc_vu=list_chuc_vu, ten=ten, role=role)

      # POST
      @app.route('/staffs/insert', methods = ['POST'])
      def insertStaff():
            if flask.request.method == 'POST':
                  # Tự động sinh mã nhân viên
                  cursor = conn.cursor()
                  sql_MaNVFinal = "Select top 1 MaNV from tNhanVien order by MaNV desc"
                  cursor.execute(sql_MaNVFinal)
                  result = cursor.fetchone()
                  s = result[0]
                  prefix = s[0:2]
                  number = int(s[2:]) + 1
                  ma =  f"{prefix}{number:02d}"
                  #ma = "NV_" + datetime.now().strftime("%Y%m%d%H%M%S")        # Tự động tạo MaNV theo ngày giờ

                  # Trích xuất dữ liệu từ form
                  ten = flask.request.form['TenNV']
                  gioitinh = flask.request.form['GioiTinh']
                  ngaysinh = flask.request.form['NgaySinh']
                  diachi = flask.request.form['DiaChi']
                  sdt = flask.request.form['SDT']
                  chucvu = flask.request.form['ChucVu']
                  
                  # Chuyển đổi chuỗi thành datetime
                  if ngaysinh != '':
                        ngaysinh = datetime.strptime(ngaysinh, '%Y-%m-%dT%H:%M')

                  # Kiểm gtra thông tin nhập vào có rỗng ko?
                  if ten == '':
                        return redirect(url_for('createStaff'))

                  cursor = conn.cursor() 
                  sql = 'INSERT INTO tNhanVien VALUES (?, ?, ?, ?, ?, ?, ?, ?)'
                  data = (ma, ten, gioitinh, ngaysinh, diachi, sdt, chucvu, 0)
                  cursor.execute(sql, data)
                  cursor.commit()
                  res = flask.jsonify({"mess":"Thêm nhân viên thành công!"})
                  res.status_code = 200
                  return redirect(url_for('staffs'))
      # ----------------------- #

      # ----- Sửa nhân viên ----- #
      @app.route('/staffs/edit/<id>')
      def editStaff(id):
            ten = session.get('MaNV')
            role = session.get('role')
            list_chuc_vu = ['Quản lý', 'Bán hàng', 'Thủ kho']
            cursor = conn.cursor()
            sql = 'SELECT * FROM tNhanVien WHERE MaNV = ?'
            cursor.execute(sql, id)
            result = cursor.fetchone()    
            result[3] = datetime.strptime(result[3], '%Y-%m-%d')
            return render_template('/staff/editStaff.html', nhanvien=result, list_chuc_vu=list_chuc_vu, id=id, ten=ten, role=role)

      # PUT by ID
      @app.route('/staffs/update/<id>', methods = ['POST'])
      def putStaff(id):
            # Trích xuất dữ liệu từ request
            ten = flask.request.form['TenNV']
            gioitinh = flask.request.form['GioiTinh']
            ngaysinh = flask.request.form['NgaySinh']
            diachi = flask.request.form['DiaChi']
            sdt = flask.request.form['SDT']
            chucvu = flask.request.form['ChucVu']
            
            # Chuyển đổi chuỗi thành datetime
            ngaysinh = datetime.strptime(ngaysinh, '%Y-%m-%dT%H:%M')

            # Thực hiện truy vấn SQL để cập nhật thuộc tính của nhân viên
            cursor = conn.cursor()
            sql = 'UPDATE tNhanVien SET TenNV=?, GioiTinh=?, NgaySinh = ?, DiaChi = ?, SDT=?, ChucVu=? WHERE MaNV=?'
            data = (ten, gioitinh, ngaysinh, diachi, sdt, chucvu, id)
            cursor.execute(sql, data)
            conn.commit()
            
            # Trả về thông báo thành công
            res = flask.jsonify({"mess": "Thanh Cong"})
            res.status_code = 200
            return redirect(url_for('staffs'))
      # ------------------------- #

      # ----- Xóa nhân viên ----- #
      @app.route('/staffs/delete/<id>')
      def deleteStaff(id):
            ten = session.get('MaNV')
            role = session.get('role')
            # Lấy thông tin nhân viên theo id
            cursor = conn.cursor()
            sql = "Select * from tNhanVien where MaNV = ?"
            cursor.execute(sql, id)
            result = cursor.fetchone()
            return render_template('/staff/deleteStaff.html', id=id, nhanvien=result, ten=ten, role=role)
      
      # Delete by ID (Chuyển thuộc tính 'isDeleted = true')
      @app.route('/staffs/deletebyid/<id>', methods = ['POST'])
      def deleteStaffById(id):
            cursor = conn.cursor()
            sql = 'Update tNhanVien set isDeleted = 1 where MaNV = ?'
            data = id
            cursor.execute(sql, data)
            conn.commit()
            
            # Trả về thông báo thành công
            res = flask.jsonify({"mess": "Thanh Cong"})
            res.status_code = 200
            return redirect(url_for('staffs'))
      # -------------------------- #
      # ----------------------------------- #


      # ---------- Bên Android app ---------- #
      @app.route('/medicines/getAll', methods=['GET'])
      def getThuoc():
            cursor = conn.cursor()
            sql = "SELECT top 8 * FROM tThuoc where isDeleted = 0"
            cursor.execute(sql) # thực thi
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            
            for i in cursor.fetchall():
                  
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
    
      @app.route('/customer/signUp', methods = ['POST'])
      def signupKH():
            cursor = conn.cursor()
            # Lấy mã khách hàng cuối cùng từ cơ sở dữ liệu
            sql_MaKHFinal = "SELECT TOP 1 MaKH FROM tKhachHang ORDER BY MaKH DESC"
            cursor.execute(sql_MaKHFinal)
            result = cursor.fetchone()
            
            tenkh = flask.request.json.get("TenKH")
            gioitinh = flask.request.json.get("GioiTinh")
            ngaysinh = flask.request.json.get("NgaySinh")
            diachi = flask.request.json.get("DiaChi")
            sodienthoai = flask.request.json.get("SDT")
            # Tách mã khách hàng thành mã KH và số
            ma_kh = result[0]
            first_ma_kh = ma_kh[0:2]  # Lấy 2 ký tự đầu tiên (mã KH)
            number_str = ma_kh[2:]  # Lấy chuỗi số sau mã KH

            # Chuyển đổi chuỗi số sang số nguyên và tăng 1
            number = int(number_str) + 1

            # Định dạng số thành chuỗi có độ dài 2 ký tự và thêm 0 vào trước nếu cần
            number_str = f"{number:02d}"

            # Tạo mã khách hàng mới
            ma_kh_moi = f"{first_ma_kh}{number_str}"
                   
            sql = "insert into tKhachHang values (?, ?, ?, ?, ?, ?)"
            data = (ma_kh_moi, tenkh, gioitinh, ngaysinh, diachi, sodienthoai)
            cursor.execute(sql, data)
            
            sql_select = "SELECT * FROM tKhachHang WHERE MaKH = ?"
            data_select = (ma_kh_moi,)
            cursor.execute(sql_select, data_select)
            keys = [i[0] for i in cursor.description]
            result = cursor.fetchone()
        
            if result:  # Kiểm tra xem kết quả có tồn tại không
                result_dict = dict(zip(keys, result))  # Chuyển hàng thành từ điển
                resp = flask.jsonify(result_dict)
                resp.status_code = 200
                return resp
            else:
                return flask.jsonify({}), 404
        
      @app.route('/customer/signUpAccountKH', methods = ['POST'])
      def signupaccountKH():
            try:
                  makh = flask.request.json.get("MaKH")
                  tentk = flask.request.json.get("TenTK")
                  passtk = flask.request.json.get("Pass")
                  
                  cursor = conn.cursor()
                  sql = "insert into tAccountKH values (?, ?, ?)"
                  data = (makh, tentk,passtk)
                  cursor.execute(sql, data)
                  cursor.commit()
                  res = flask.jsonify({"mess":"thành công"})
                  res.status_code = 200
                  return res
            except Exception as e:
                  res = flask.jsonify({"mess":e})
                  res.status_code = 200
                  return res
       
      @app.route('/customer/login/<TenTK>/<Pass>', methods=['GET'])
      def searchBooks(TenTK = 'A', Pass = 'A'):
            cursor = conn.cursor()
            # Sử dụng LIKE để tìm các sách có tên gần giống với từ khóa
            sql = "SELECT * FROM tAccountKH where TenTK = ? and Pass = ?"
            data = (TenTK, Pass)
            cursor.execute(sql, data)
            keys = [i[0] for i in cursor.description]  # Lấy keys trực tiếp từ cursor.description
            result = cursor.fetchone()  # Chỉ lấy một hàng đầu tiên từ kết quả
            
            if result:  # Kiểm tra xem kết quả có tồn tại không
                result_dict = dict(zip(keys, result))  # Chuyển hàng thành từ điển
                resp = flask.jsonify(result_dict)
                resp.status_code = 200
                return resp
            else:
                return flask.jsonify({}), 404
        
      @app.route('/medicine/getAllLoaiThuoc', methods = ['GET'])
      def getDanhSachSanPham():
            cursor = conn.cursor()
            sql = "select distinct MaLoai, TenLoai from tLoaiThuoc"
            cursor.execute(sql) # thực thi
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
    
      @app.route('/medicine/getThuocByMaLoai/<MaLoai>', methods = ['GET'])
      def getThuocByMaLoai(MaLoai = 'A'):
            cursor = conn.cursor()
            sql = "select * from tThuoc where MaLoai = ? and isDeleted = 0"
            data = MaLoai
            cursor.execute(sql, data) # thực thi
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
    
      @app.route('/staffs/getAllStaffs', methods = ['GET'])
      def getNhanVien():
            cursor = conn.cursor()
            sql = "select MaNV from tNhanVien"
            cursor.execute(sql) # thực thi
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
    
      @app.route('/bill/billOfSale', methods = ['POST'])
      def hoadonban():
            try:
                  sohdb = flask.request.json.get("SoHDB")
                  manv = flask.request.json.get("MaNV")
                  ngaylap = flask.request.json.get("NgayLap")
                  makh = flask.request.json.get("MaKH")
                  tongtien = flask.request.json.get("TongTien")
                  cursor = conn.cursor()
                  sql = "insert into tHoaDonBan values (?, ?, ?, ?, ?)"
                  data = (sohdb, manv, ngaylap, makh, tongtien)
                  cursor.execute(sql, data)
                  cursor.commit()
                  res = flask.jsonify({"mess":"thành công"})
                  res.status_code = 200
                  return res
            except Exception as e:
                  res = flask.jsonify({"mess":e})
                  res.status_code = 200
                  return res
    
      @app.route('/bill/salesInvoiceDetails', methods = ['POST'])
      def chitiethoadonban():
            chi_tiet_hdb_list = flask.request.get_json()
            cursor = conn.cursor()
            # Insert each ChiTietHDB object into the database
            for chi_tiet_hdb in chi_tiet_hdb_list:
                  sohdb = chi_tiet_hdb.get("SoHDB")
                  mathuoc = chi_tiet_hdb.get("MaThuoc")
                  soluongban = chi_tiet_hdb.get("SLBan")
                  khuyenmai = chi_tiet_hdb.get("KhuyenMai", 0.0)  # Default to 0 if not provided
                  thanhtien = chi_tiet_hdb.get("ThanhTien")
                  # Input validation (consider using a validation library)
                  if not sohdb or not mathuoc or soluongban <= 0 or thanhtien <= 0:
                        return flask.jsonify({"error": "Invalid data in ChiTietHDB object"}), 400
                  sql = "INSERT INTO tChiTietHDB (SoHDB, MaThuoc, SLBan, KhuyenMai, ThanhTien) VALUES (?, ?, ?, ?, ?)"
                  data = (sohdb, mathuoc, soluongban, khuyenmai, thanhtien)
                  cursor.execute(sql, data)
            conn.commit()
            res = flask.jsonify({"mess":"thành công"})
            res.status_code = 200
            return res
            
      @app.route('/customer/getInfoCustomer/<MaKH>', methods = ['GET'])
      def getThongTinKhachHang(MaKH = 'A'):
            cursor = conn.cursor()
            sql = "select * from tKhachHang WHERE MaKH = ?"
            data = MaKH
            cursor.execute(sql, data) # thực thi
            keys = [i[0] for i in cursor.description]  # Lấy keys trực tiếp từ cursor.description
            result = cursor.fetchone()  # Chỉ lấy một hàng đầu tiên từ kết quả
            
            if result:  # Kiểm tra xem kết quả có tồn tại không
                  result_dict = dict(zip(keys, result))  # Chuyển hàng thành từ điển
                  resp = flask.jsonify(result_dict)
                  resp.status_code = 200
                  return resp
            else:
                  return flask.jsonify({"message": "Không tìm thấy sách với ID đã cung cấp."}), 404

      # Tìm kiếm thuốc theo tên
      @app.route('/medicine/getThuocByTenThuoc/<TenThuoc>', methods=['GET'])
      def getThuocByTenThuoc(TenThuoc='A'):
            cursor = conn.cursor()
            sql = "SELECT * FROM tThuoc WHERE TenThuoc LIKE ? and isDeleted = 0"
            # Thêm dấu '%' vào cả hai bên của giá trị TenThuoc
            data = '%' + TenThuoc + '%'
            cursor.execute(sql, (data,))
            results = []
            keys = [i[0] for i in cursor.description] # Sử dụng list comprehension
            for row in cursor.fetchall():
                  results.append(dict(zip(keys, row)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      
      @app.route('/medicine/getThuocByMaThuoc/<MaThuoc>', methods=['GET'])
      def getThuocByMaThuoc(MaThuoc='A'):
            cursor = conn.cursor()
            sql = "SELECT * FROM tThuoc WHERE MaThuoc = ? and isDeleted = 0"
            # Thêm dấu '%' vào cả hai bên của giá trị TenThuoc
            data = MaThuoc
            cursor.execute(sql, (data,))
            keys = [i[0] for i in cursor.description]  # Lấy keys trực tiếp từ cursor.description
            result = cursor.fetchone()  # Chỉ lấy một hàng đầu tiên từ kết quả
            
            if result:  # Kiểm tra xem kết quả có tồn tại không
                  result_dict = dict(zip(keys, result))  # Chuyển hàng thành từ điển
                  resp = flask.jsonify(result_dict)
                  resp.status_code = 200
                  return resp
            else:
                  return flask.jsonify({"message": "Không tìm thấy sách với ID đã cung cấp."}), 404
      
      @app.route('/medicine/medicineBestSeller', methods = ['GET'])
      def getTop3BestSeller():
            cursor = conn.cursor()
            sql = "select * from tThuoc where isDeleted = 0 and MaThuoc in (select Top 3 tThuoc.MaThuoc  from tThuoc inner join tChiTietHDB on tThuoc.MaThuoc = tChiTietHDB.MaThuoc group by tThuoc.MaThuoc, TenThuoc,ThanhPhan,NgaySX,NgayHH, MaLoai, DonGiaBan, DonGiaNhap, SoLuong, TrongLuong, Anh, XuatXu, MoTa order by sum(tChiTietHDB.SLBan) DESC)"
            cursor.execute(sql) # thực thi
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            
            for i in cursor.fetchall():
                  
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      
      
      @app.route('/medicine/updateSLThuoc/<MaThuoc>/<SoLuong>', methods=['POST'])
      def updateCustomer(MaThuoc = "", SoLuong = 1):
            try:
                  # Nhận thông tin khách hàng từ request
                  cursor = conn.cursor()
                  # Câu lệnh SQL để cập nhật thông tin khách hàng
                  sql = "UPDATE tThuoc SET SoLuong = ? WHERE MaThuoc=? and isDeleted = 0"
                  data = (SoLuong, MaThuoc)
                  cursor.execute(sql, data)
                  conn.commit()
                  res = flask.jsonify()
                  res.status_code = 200
                  return res
            except Exception as e:
                  # Trả về thông báo lỗi nếu có lỗi xảy ra
                  res = flask.jsonify()
                  res.status_code = 500
                  return res
            
      @app.route('/customer/updateInfoCustomer', methods=['POST'])
      def updateInfoCustomer():
            try:
                  makh = flask.request.json.get("MaKH")
                  tenkh = flask.request.json.get("TenKH")
                  gioitinh = flask.request.json.get("GioiTinh")
                  ngaysinh = flask.request.json.get("NgaySinh")
                  diachi = flask.request.json.get("DiaChi")
                  sodienthoai = flask.request.json.get("SDT")
                  
                  cursor = conn.cursor()
                  sql = "update tKhachHang set TenKH = ?, GioiTinh = ?,NgaySinh = ?, DiaChi = ?, SDT = ? where MaKH = ?"
                  data = (tenkh, gioitinh, ngaysinh, diachi, sodienthoai, makh)
                  cursor.execute(sql, data)
                  cursor.commit()
                  # res = flask.jsonify({"thành công"})
                  # res.status_code = 200
                  # return res
                  return "Thành Công", 200
            except Exception as e:
                  # res = flask.jsonify({"mess":e})
                  # res.status_code = 200
                  # return res
                  return "Lỗi", 500
            
      @app.route('/thuoc/updatetongtien/<SoHDB>/<TongTien>', methods=['POST'])
      def updateTongTien(SoHDB = "", TongTien = 1):
            try:
                  # Nhận thông tin khách hàng từ request
                  cursor = conn.cursor()
                  # Câu lệnh SQL để cập nhật thông tin khách hàng
                  sql = "UPDATE tHoaDonBan SET TongTien = ? WHERE SoHDB = ?"
                  data = (TongTien, SoHDB)
                  cursor.execute(sql, data)
                  conn.commit()  
                  # Tạo response thành công
                  res = flask.jsonify()
                  res.status_code = 200
                  return res
            except Exception as e:
                  # Trả về thông báo lỗi nếu có lỗi xảy ra
                  res = flask.jsonify()
                  res.status_code = 500
                  return res
            
      
      @app.route('/goods/getHDBIdWithMaNVNull/<MaKH>', methods = ['GET'])
      def getHDBIdWithMaNVNull(MaKH = "A"):
            cursor = conn.cursor()
            # Câu lệnh SQL để cập nhật thông tin khách hàng
            sql = "select distinct tChiTietHDB.SoHDB FROM tChiTietHDB inner join tHoaDonBan on tChiTietHDB.SoHDB = tHoaDonBan.SoHDB where MaKH = ? and MaNV IS NULL "
            data = (MaKH)
            cursor.execute(sql, data)

            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      
      @app.route('/goods/getHDBIdWithMaNVNotNull/<MaKH>', methods = ['GET'])
      def getHDBIdWithMaNVNotNull(MaKH = "A"):
            cursor = conn.cursor()
            # Câu lệnh SQL để cập nhật thông tin khách hàng
            sql = "select distinct tChiTietHDB.SoHDB FROM tChiTietHDB inner join tHoaDonBan on tChiTietHDB.SoHDB = tHoaDonBan.SoHDB where MaKH = ? and MaNV IS NOT NULL "
            data = (MaKH)
            cursor.execute(sql, data)
            
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))
            
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      
      
      @app.route('/goods/getHDBDetailWithMaNVNull/<SoHDB>', methods = ['GET'])
      def getHDBDetailWithMaNVNull(SoHDB = "SoHDB"):
            cursor = conn.cursor()
            # Câu lệnh SQL để cập nhật thông tin khách hàng
            sql = "select distinct tChiTietHDB.SoHDB, tThuoc.MaThuoc, TenThuoc, SLBan, DonGiaBan, ThanhTien, Anh, MaKH from tThuoc inner join tChiTietHDB on tThuoc.MaThuoc = tChiTietHDB.MaThuoc inner join tHoaDonBan on tChiTietHDB.SoHDB = tHoaDonBan.SoHDB where tHoaDonBan.SoHDB = ? and MaNV IS NULL "
            data = (SoHDB)
            cursor.execute(sql, data)
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))                
            cursor.commit()
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp
      
      @app.route('/goods/getHDBDetailWithMaNVNotNull/<SoHDB>', methods = ['GET'])
      def getHDBDetailWithMaNVNotNull(SoHDB = "SoHDB"):
            cursor = conn.cursor()
            # Câu lệnh SQL để cập nhật thông tin khách hàng
            sql = "select distinct tChiTietHDB.SoHDB, tThuoc.MaThuoc, TenThuoc, SLBan, DonGiaBan, ThanhTien, Anh, MaKH from tThuoc inner join tChiTietHDB on tThuoc.MaThuoc = tChiTietHDB.MaThuoc inner join tHoaDonBan on tChiTietHDB.SoHDB = tHoaDonBan.SoHDB where tHoaDonBan.SoHDB = ? and MaNV IS NOT NULL "
            data = (SoHDB)
            cursor.execute(sql, data)
            results1 = []
            results = [] # chưa kết quả trả về
            keys = []
            for i in cursor.description:
                  keys.append(i[0]) # lấy keys
            for i in cursor.fetchall():
                  results.append(dict(zip(keys, i)))                
            cursor.commit()
            resp = flask.jsonify(results)
            resp.status_code = 200        
            return resp

except Exception as e:
      print(e)

if __name__ == "__main__":
      app.secret_key = 'secret_key'
      app.run(debug=True)                           # Dùng để chạy debug với Web
      app.run(host = "192.168.29.106", port = 5000)     # Dùng để chạy với Web và App (Android)

