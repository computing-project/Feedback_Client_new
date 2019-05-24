package com.example.feedback;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CommunicationForClient {
	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private String host;
	private OkHttpClient client;
	private String token;
	//private String myUsername;
	AllFunctions functions;

	public CommunicationForClient(AllFunctions functions) {
		host = "http://54.206.24.105:8888/RapidFeedback/";
		client = new OkHttpClient();
		this.functions = functions;
	}


	public void register(String firstName, String middleName, String lastName,
						 String email, String password) {
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("firstName", firstName);
		jsonSend.put("middleName", middleName);
		jsonSend.put("lastName", lastName);
		jsonSend.put("email", email);
		jsonSend.put("password", password);
		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "RegisterServlet")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();
			System.out.println("Receive: " + receive); //just for test
			JSONObject jsonReceive = JSONObject.parseObject(receive);

			String register_ACK_String = jsonReceive.get("register_ACK").toString();
			if(register_ACK_String.equals("true"))
				functions.registerACK(true);
			else
				functions.registerACK(false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void login(String username, String password) {
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("username", username);
		jsonSend.put("password", password);
		System.out.println("Send: " + jsonSend.toJSONString()); //just for test


		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "LoginServlet")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();
			System.out.println("Receive: " + receive); //just for test
			JSONObject jsonReceive = JSONObject.parseObject(receive);
			int login_ACK = Integer.parseInt(jsonReceive.get("login_ACK").toString());
			if (login_ACK > 0)
			{
				//get projectlist from jsonReceive
				String projectListString = jsonReceive.get("projectList").toString();
				String firstName = jsonReceive.getString("firstName");
				List<ProjectInfo> projectList = JSONObject.parseArray(projectListString, ProjectInfo.class);
				ArrayList<ProjectInfo> arrayList ;
				arrayList = new ArrayList();
				arrayList.addAll(projectList);
				functions.setUsername(firstName);

				functions.loginSucc(arrayList);

				token = jsonReceive.getString("token");
			}
			else {
				functions.loginFail();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void updateProject_About(String projectName, String subjectName,
									String subjectCode, String description) {
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("subjectName", subjectName);
		jsonSend.put("subjectCode", subjectCode);
		jsonSend.put("description", description);
		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "UpdateProject_About_Servlet")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();
			System.out.println("Receive: " + receive); //just for test
			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateProject_ACK = jsonReceive.get("updateProject_ACK").toString();
			if (updateProject_ACK.equals("true")) {
				;
			} else {
				;
			}
		} catch (IOException e1) {
			System.out.println("updateProjectAbout: something wrong in receiving response from server.");
		}
	}

	public void deleteProject(String projectName)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "DeleteProjectServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateProject_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (Exception e1) {
			System.out.println("Exception in Delete Project!");
		}
	}


	public void criteriaListSend(String projectName, ArrayList<Criteria> markedCriteriaList, ArrayList<Criteria> commentCriteriaList) {
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);

		String markedCriteriaListString = com.alibaba.fastjson.JSON.toJSONString(markedCriteriaList);
		jsonSend.put("markedCriteriaList", markedCriteriaListString);
		String commentCriteriaListString = com.alibaba.fastjson.JSON.toJSONString(commentCriteriaList);
		jsonSend.put("commentCriteriaList", commentCriteriaListString);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "CriteriaListServlet")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();
			System.out.println("Receive: " + receive); //just for test
			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateProject_ACK = jsonReceive.get("updateProject_ACK").toString();
			if (updateProject_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			System.out.println("updateProjectAbout: something wrong in receiving response from server.");
		}
	}



	public void updateProject_Time(String projectName, int durationMin, int durationSec,
								   int warningMin, int warningSec) {
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("durationMin", durationMin);
		jsonSend.put("durationSec", durationSec);
		jsonSend.put("warningMin", warningMin);
		jsonSend.put("warningSec", warningSec);
		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "UpdateProject_Time_Servlet")
				.post(body)
				.build();
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();
			System.out.println("Receive: " + receive); //just for test
			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateProject_ACK = jsonReceive.get("updateProject_ACK").toString();
			if (updateProject_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void addStudent(String projectName, String studentID, String firstName,
						   String middleName, String lastName, String email)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);
		jsonSend.put("firstName", firstName);
		jsonSend.put("middleName", middleName);
		jsonSend.put("lastName", lastName);
		jsonSend.put("email", email);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "AddStudentServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateStudent_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (Exception e1) {
			System.out.println("Exception in addStudent communication");
		}
	}

	public void editStudent(String projectName, String studentID, String firstName,
						   String middleName, String lastName, String email)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);
		jsonSend.put("firstName", firstName);
		jsonSend.put("middleName", middleName);
		jsonSend.put("lastName", lastName);
		jsonSend.put("email", email);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "EditStudentServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateStudent_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void groupStudent(String projectName, String studentID, int groupNumber)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);
		jsonSend.put("group", groupNumber);

		System.out.println("Send in group student method: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "GroupStudentServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateStudent_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}



	public void deleteStudent(String projectName, String studentID)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "DeleteStudentServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateStudent_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void inviteAssessor(String projectName, String assessorEmail)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("assessorEmail", assessorEmail);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "InviteAssessorServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String invite_ACK = jsonReceive.get("sendMail_ACK").toString();
			if (invite_ACK.equals("true")) {
				AllFunctions.getObject().inviteAssessor_Success(projectName, assessorEmail);
			} else {
				AllFunctions.getObject().inviteAssessor_Fail();
			}
		} catch (Exception e1) {
			System.out.println("exception in invite_Assessor!");
		}
	}

	public void deleteAssessor(String projectName, String assessorEmail)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("assessorEmail", assessorEmail);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "InviteAssessorServlet")
				.delete(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String delete_ACK = jsonReceive.get("delete_ACK").toString();
			if (delete_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (Exception e1) {
			System.out.println("exception in delete_Assessor!");
		}
	}



	public void sendMark(String projectName, String studentID, Mark mark)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);
		String markString = com.alibaba.fastjson.JSON.toJSONString(mark);
		jsonSend.put("mark", markString);

		System.out.println("Send in method sendMark: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "MarkServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String mark_ACK = jsonReceive.get("mark_ACK").toString();
			if (mark_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}



	public void importStudents(String projectName, ArrayList<StudentInfo> studentList)
	{
		//construct JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("token", token);
		jsonSend.put("projectName", projectName);
		String studentListString = com.alibaba.fastjson.JSON.toJSONString(studentList);
		jsonSend.put("studentList", studentListString);

		System.out.println("Send: " + jsonSend.toJSONString()); //just for test

		RequestBody body = RequestBody.create(JSON, jsonSend.toJSONString());
		Request request = new Request.Builder()
				.url(host + "ImportStudentsServlet")
				.post(body)
				.build();

		//get the JSONObject from response
		try (Response response = client.newCall(request).execute()) {
			String receive = response.body().string();

			System.out.println("Receive: " + receive); //just for test

			JSONObject jsonReceive = JSONObject.parseObject(receive);
			String updateStudent_ACK = jsonReceive.get("updateStudent_ACK").toString();
			if (updateStudent_ACK.equals("true")) {
				;
			} else {
				//失败跳出
			}
		} catch (Exception e1) {
			System.out.println("Exception in import student in communication!");
		}
	}

}

