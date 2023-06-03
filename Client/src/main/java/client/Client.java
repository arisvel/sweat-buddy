package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxResults;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import dependencies.Utilities;
import dependencies.fileprocessing.gpx.GpxFile;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String username;
//    private GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Gson gson = new Gson();

    public Client(String host_address, int port) {

        try {
            this.socket = new Socket(host_address, port);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientInfo(TransmissionObject to) {
        try {
            String jsonString = gson.toJson(to);

            this.outputStream.writeObject(jsonString);
            this.outputStream.flush();


        } catch (IOException e) {
            System.out.println("error");
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Client c = new Client(Utilities.HOST_ADDRESS, Utilities.CLIENTS_PORT);

        Scanner s = new Scanner(System.in);
        System.out.print("Username: ");
        String username = s.nextLine();
        System.out.println("Password: ");
        String password = s.nextLine();

        TransmissionObject to = new TransmissionObject();
        to.type = TransmissionObjectType.LOGIN_MESSAGE;
        to.username = username;
        to.password = password;
        c.sendClientInfo(to);

        String answer = (String) c.inputStream.readObject();
        System.out.println(answer);

        TimeUnit.SECONDS.sleep(2);

        TransmissionObject to2 = new TransmissionObject();
        to2.type = TransmissionObjectType.GPX_FILE;
        to2.gpxFile = gpxFile;
        to2.message = "Monastiraki Athens";
        c.sendClientInfo(to2);
        String answer2 = (String) c.inputStream.readObject();
        System.out.println(answer2);
        while (true) {

        }

    }

    static String gpxFile = """
            <?xml version="1.0"?>
            <gpx version="1.1" creator="user1">
            <wpt lat="37.975033880710484" lon="23.734082814713215">
                <ele>84.49</ele>
                <time>2023-03-19T17:57:19Z</time>
            </wpt>
            <wpt lat="37.97466276547986" lon="23.734044934572992">
                <ele>85.02</ele>
                <time>2023-03-19T17:57:29Z</time>
            </wpt>
            <wpt lat="37.974307555569304" lon="23.733943010630426">
                <ele>85.99</ele>
                <time>2023-03-19T17:57:39Z</time>
            </wpt>
            <wpt lat="37.97407497672046" lon="23.733916188540277">
                <ele>86.93</ele>
                <time>2023-03-19T17:57:45Z</time>
            </wpt>
            <wpt lat="37.97368170535409" lon="23.7338303578518">
                <ele>88.70</ele>
                <time>2023-03-19T17:57:56Z</time>
            </wpt>
            <wpt lat="37.973432209523935" lon="23.733760620417414">
                <ele>89.92</ele>
                <time>2023-03-19T17:58:03Z</time>
            </wpt>
            <wpt lat="37.973157340254694" lon="23.733690882983026">
                <ele>90.74</ele>
                <time>2023-03-19T17:58:11Z</time>
            </wpt>
            <wpt lat="37.97293744409827" lon="23.733680154146967">
                <ele>91.16</ele>
                <time>2023-03-19T17:58:17Z</time>
            </wpt>
            <wpt lat="37.97273869123628" lon="23.73357286578637">
                <ele>91.75</ele>
                <time>2023-03-19T17:58:23Z</time>
            </wpt>
            <wpt lat="37.97255685304009" lon="23.733548725905237">
                <ele>92.03</ele>
                <time>2023-03-19T17:58:28Z</time>
            </wpt>
            <wpt lat="37.97242576010758" lon="23.73347362405282">
                <ele>92.03</ele>
                <time>2023-03-19T17:58:32Z</time>
            </wpt>
            <wpt lat="37.97226295081712" lon="23.733430708708582">
                <ele>91.62</ele>
                <time>2023-03-19T17:58:36Z</time>
            </wpt>
            <wpt lat="37.97206029751553" lon="23.73372561207019">
                <ele>89.90</ele>
                <time>2023-03-19T17:58:44Z</time>
            </wpt>
            <wpt lat="37.97201800921233" lon="23.733830218221772">
                <ele>89.84</ele>
                <time>2023-03-19T17:58:46Z</time>
            </wpt>
            <wpt lat="37.97179215588327" lon="23.73358886353126">
                <ele>88.62</ele>
                <time>2023-03-19T17:58:54Z</time>
            </wpt>
            <wpt lat="37.97161031534244" lon="23.733449388662486">
                <ele>87.73</ele>
                <time>2023-03-19T17:59:00Z</time>
            </wpt>
            <wpt lat="37.9715215093332" lon="23.733393062273173">
                <ele>87.31</ele>
                <time>2023-03-19T17:59:02Z</time>
            </wpt>
            <wpt lat="37.971445389811215" lon="23.733317960420756">
                <ele>86.89</ele>
                <time>2023-03-19T17:59:04Z</time>
            </wpt>
            <wpt lat="37.971352354732716" lon="23.733240176359324">
                <ele>86.34</ele>
                <time>2023-03-19T17:59:07Z</time>
            </wpt>
            <wpt lat="37.971244518471444" lon="23.7331275235807">
                <ele>85.66</ele>
                <time>2023-03-19T17:59:10Z</time>
            </wpt>
            <wpt lat="37.971085935446546" lon="23.7331275235807">
                <ele>85.00</ele>
                <time>2023-03-19T17:59:14Z</time>
            </wpt>
            <wpt lat="37.97096964101056" lon="23.73317312113395">
                <ele>84.38</ele>
                <time>2023-03-19T17:59:17Z</time>
            </wpt>
            <wpt lat="37.970859689738035" lon="23.73322676531425">
                <ele>83.99</ele>
                <time>2023-03-19T17:59:20Z</time>
            </wpt>
            <wpt lat="37.970758196109514" lon="23.733296502748637">
                <ele>83.54</ele>
                <time>2023-03-19T17:59:23Z</time>
            </wpt>
            <wpt lat="37.97068419025026" lon="23.733382333437113">
                <ele>83.23</ele>
                <time>2023-03-19T17:59:25Z</time>
            </wpt>
            <wpt lat="37.97062921442083" lon="23.73348962179771">
                <ele>82.93</ele>
                <time>2023-03-19T17:59:27Z</time>
            </wpt>
            <wpt lat="37.970559064635104" lon="23.733678812235546">
                <ele>82.44</ele>
                <time>2023-03-19T17:59:31Z</time>
            </wpt>
            <wpt lat="37.970508317630575" lon="23.733770007342052">
                <ele>82.25</ele>
                <time>2023-03-19T17:59:33Z</time>
            </wpt>
            <wpt lat="37.97044065490332" lon="23.733914846628856">
                <ele>81.91</ele>
                <time>2023-03-19T17:59:36Z</time>
            </wpt>
            <wpt lat="37.97038567889148" lon="23.73409187242384">
                <ele>81.42</ele>
                <time>2023-03-19T17:59:40Z</time>
            </wpt>
            <wpt lat="37.97035184747912" lon="23.73416965648527">
                <ele>81.16</ele>
                <time>2023-03-19T17:59:42Z</time>
            </wpt>
            <wpt lat="37.97029687140082" lon="23.73432790681715">
                <ele>80.69</ele>
                <time>2023-03-19T17:59:45Z</time>
            </wpt>
            <wpt lat="37.97025246761518" lon="23.734435195177745">
                <ele>80.36</ele>
                <time>2023-03-19T17:59:47Z</time>
            </wpt>
            <wpt lat="37.970159431024875" lon="23.734620267599773">
                <ele>79.78</ele>
                <time>2023-03-19T17:59:51Z</time>
            </wpt>
            <wpt lat="37.970058486292615" lon="23.734862899973006">
                <ele>79.06</ele>
                <time>2023-03-19T17:59:57Z</time>
            </wpt>
            <wpt lat="37.969967563931355" lon="23.73514453191957">
                <ele>78.31</ele>
                <time>2023-03-19T18:00:03Z</time>
            </wpt>
            <wpt lat="37.96985549667996" lon="23.73540738840303">
                <ele>77.58</ele>
                <time>2023-03-19T18:00:09Z</time>
            </wpt>
            <wpt lat="37.96975400166315" lon="23.735584414198012">
                <ele>77.11</ele>
                <time>2023-03-19T18:00:13Z</time>
            </wpt>
            <wpt lat="37.96967999479161" lon="23.735788262083144">
                <ele>76.67</ele>
                <time>2023-03-19T18:00:18Z</time>
            </wpt>
            <wpt lat="37.96958272850412" lon="23.736083305074782">
                <ele>76.10</ele>
                <time>2023-03-19T18:00:25Z</time>
            </wpt>
            <wpt lat="37.96944528679123" lon="23.736425700460426">
                <ele>75.68</ele>
                <time>2023-03-19T18:00:33Z</time>
            </wpt>
            <wpt lat="37.96937973757602" lon="23.73670733240699">
                <ele>75.41</ele>
                <time>2023-03-19T18:00:39Z</time>
            </wpt>
            <wpt lat="37.96930361583327" lon="23.736988964353554">
                <ele>75.21</ele>
                <time>2023-03-19T18:00:45Z</time>
            </wpt>
            <wpt lat="37.96921879696766" lon="23.737450024549535">
                <ele>75.66</ele>
                <time>2023-03-19T18:00:55Z</time>
            </wpt>
            <wpt lat="37.969209670236594" lon="23.73783730215352">
                <ele>76.64</ele>
                <time>2023-03-19T18:01:03Z</time>
            </wpt>
            <wpt lat="37.9692604181388" lon="23.73827182001393">
                <ele>78.07</ele>
                <time>2023-03-19T18:01:12Z</time>
            </wpt>
            <wpt lat="37.969281563087705" lon="23.73844079918187">
                <ele>78.62</ele>
                <time>2023-03-19T18:01:15Z</time>
            </wpt>
            <wpt lat="37.96944860796988" lon="23.738821672861985">
                <ele>79.89</ele>
                <time>2023-03-19T18:01:24Z</time>
            </wpt>
            <wpt lat="37.969575477246735" lon="23.739105987017563">
                <ele>80.79</ele>
                <time>2023-03-19T18:01:31Z</time>
            </wpt>
            <wpt lat="37.969676823613106" lon="23.739330385928497">
                <ele>81.30</ele>
                <time>2023-03-19T18:01:36Z</time>
            </wpt>
            <wpt lat="37.96979100561718" lon="23.739614700084076">
                <ele>81.58</ele>
                <time>2023-03-19T18:01:43Z</time>
            </wpt>
            <wpt lat="37.97000099514987" lon="23.739977014286477">
                <ele>81.80</ele>
                <time>2023-03-19T18:01:53Z</time>
            </wpt>
            <wpt lat="37.97013209241241" lon="23.740255964024026">
                <ele>81.86</ele>
                <time>2023-03-19T18:02:00Z</time>
            </wpt>
            <wpt lat="37.970367496397444" lon="23.74055933622227">
                <ele>82.02</ele>
                <time>2023-03-19T18:02:09Z</time>
            </wpt>
            <wpt lat="37.97066375502689" lon="23.74101125739611">
                <ele>82.18</ele>
                <time>2023-03-19T18:02:22Z</time>
            </wpt>
            <wpt lat="37.97098201665997" lon="23.74148752102916">
                <ele>82.33</ele>
                <time>2023-03-19T18:02:36Z</time>
            </wpt>
            <wpt lat="37.97116808763776" lon="23.741747695303605">
                <ele>82.48</ele>
                <time>2023-03-19T18:02:43Z</time>
            </wpt>
            <wpt lat="37.971395981174176" lon="23.74210098742876">
                <ele>83.28</ele>
                <time>2023-03-19T18:02:53Z</time>
            </wpt>
            <wpt lat="37.971563021245295" lon="23.74237993716631">
                <ele>84.04</ele>
                <time>2023-03-19T18:03:00Z</time>
            </wpt>
            <wpt lat="37.971785616056515" lon="23.742653869116392">
                <ele>84.78</ele>
                <time>2023-03-19T18:03:08Z</time>
            </wpt>
            <wpt lat="37.97197591383155" lon="23.743007920706358">
                <ele>85.77</ele>
                <time>2023-03-19T18:03:17Z</time>
            </wpt>
            <wpt lat="37.97219532973358" lon="23.743307025463363">
                <ele>86.43</ele>
                <time>2023-03-19T18:03:26Z</time>
            </wpt>
            <wpt lat="37.97233276629795" lon="23.7435296488116">
                <ele>86.93</ele>
                <time>2023-03-19T18:03:32Z</time>
            </wpt>
            <wpt lat="37.97252729145694" lon="23.743819327385207">
                <ele>87.68</ele>
                <time>2023-03-19T18:03:40Z</time>
            </wpt>
            <wpt lat="37.97270490093452" lon="23.744063408405562">
                <ele>88.32</ele>
                <time>2023-03-19T18:03:47Z</time>
            </wpt>
            <wpt lat="37.97283176458395" lon="23.7442323875735">
                <ele>88.89</ele>
                <time>2023-03-19T18:03:52Z</time>
            </wpt>
            <wpt lat="37.97300282584959" lon="23.744505261189364">
                <ele>89.74</ele>
                <time>2023-03-19T18:03:59Z</time>
            </wpt>
            <wpt lat="37.97311277391223" lon="23.744652782685183">
                <ele>90.10</ele>
                <time>2023-03-19T18:04:03Z</time>
            </wpt>
            <wpt lat="37.97319312046922" lon="23.744794939762972">
                <ele>90.42</ele>
                <time>2023-03-19T18:04:06Z</time>
            </wpt>
            <wpt lat="37.973392483576625" lon="23.745067394258616">
                <ele>91.09</ele>
                <time>2023-03-19T18:04:14Z</time>
            </wpt>
            <wpt lat="37.97356719925303" lon="23.745331946851927">
                <ele>91.80</ele>
                <time>2023-03-19T18:04:21Z</time>
            </wpt>
            <wpt lat="37.97374269184847" lon="23.745533112528044">
                <ele>92.41</ele>
                <time>2023-03-19T18:04:27Z</time>
            </wpt>
            <wpt lat="37.97394989943305" lon="23.745828155519682">
                <ele>93.06</ele>
                <time>2023-03-19T18:04:35Z</time>
            </wpt>
            <wpt lat="37.97415902197752" lon="23.746117473802133">
                <ele>93.86</ele>
                <time>2023-03-19T18:04:43Z</time>
            </wpt>
            <wpt lat="37.97427954006226" lon="23.746385694703623">
                <ele>94.54</ele>
                <time>2023-03-19T18:04:49Z</time>
            </wpt>
            <wpt lat="37.97450227918583" lon="23.74673845820651">
                <ele>95.73</ele>
                <time>2023-03-19T18:04:59Z</time>
            </wpt>
            <wpt lat="37.9747052559496" lon="23.746971810390807">
                <ele>96.69</ele>
                <time>2023-03-19T18:05:06Z</time>
            </wpt>
            <wpt lat="37.974895546155864" lon="23.747250760128356">
                <ele>97.78</ele>
                <time>2023-03-19T18:05:14Z</time>
            </wpt>
            <wpt lat="37.975097289954334" lon="23.74754840362165">
                <ele>98.93</ele>
                <time>2023-03-19T18:05:22Z</time>
            </wpt>
            <wpt lat="37.975255864313546" lon="23.747797849060035">
                <ele>99.96</ele>
                <time>2023-03-19T18:05:29Z</time>
            </wpt>
            <wpt lat="37.975410209694175" lon="23.748039247871375">
                <ele>100.95</ele>
                <time>2023-03-19T18:05:36Z</time>
            </wpt>
            <wpt lat="37.9756519672763" lon="23.74834417878038">
                <ele>102.55</ele>
                <time>2023-03-19T18:05:45Z</time>
            </wpt>
            <wpt lat="37.975785168754456" lon="23.748593624218763">
                <ele>103.77</ele>
                <time>2023-03-19T18:05:51Z</time>
            </wpt>
            <wpt lat="37.97598647004297" lon="23.748818819311296">
                <ele>105.11</ele>
                <time>2023-03-19T18:05:58Z</time>
            </wpt>
            <wpt lat="37.97621481424548" lon="23.748797361639177">
                <ele>105.91</ele>
                <time>2023-03-19T18:06:04Z</time>
            </wpt>
            <wpt lat="37.97652984350689" lon="23.74847013213936">
                <ele>106.64</ele>
                <time>2023-03-19T18:06:15Z</time>
            </wpt>
            <wpt lat="37.97628247165052" lon="23.74706197240654">
                <ele>104.67</ele>
                <time>2023-03-19T18:06:47Z</time>
            </wpt>
            <wpt lat="37.97596638184659" lon="23.745779343602294">
                <ele>103.78</ele>
                <time>2023-03-19T18:07:17Z</time>
            </wpt>
            <wpt lat="37.97562386411712" lon="23.74436015004654">
                <ele>102.99</ele>
                <time>2023-03-19T18:07:50Z</time>
            </wpt>
            <wpt lat="37.97531940257132" lon="23.74304978841322">
                <ele>102.28</ele>
                <time>2023-03-19T18:08:20Z</time>
            </wpt>
            <wpt lat="37.975425118528946" lon="23.74203054898756">
                <ele>102.71</ele>
                <time>2023-03-19T18:08:43Z</time>
            </wpt>
            <wpt lat="37.97556788662639" lon="23.741168065469758">
                <ele>103.18</ele>
                <time>2023-03-19T18:09:02Z</time>
            </wpt>
            <wpt lat="37.975627087381" lon="23.740433140199677">
                <ele>103.86</ele>
                <time>2023-03-19T18:09:18Z</time>
            </wpt>
            <wpt lat="37.97580791475487" lon="23.739079062557703">
                <ele>104.01</ele>
                <time>2023-03-19T18:09:48Z</time>
            </wpt>
            <wpt lat="37.97592208722268" lon="23.73839241704989">
                <ele>102.22</ele>
                <time>2023-03-19T18:10:03Z</time>
            </wpt>
            <wpt lat="37.97597671207958" lon="23.737576264541115">
                <ele>99.77</ele>
                <time>2023-03-19T18:10:21Z</time>
            </wpt>
            <wpt lat="37.976670199774524" lon="23.737769383590187">
                <ele>100.47</ele>
                <time>2023-03-19T18:10:41Z</time>
            </wpt>
            <wpt lat="37.97698311280866" lon="23.737796205680336">
                <ele>100.73</ele>
                <time>2023-03-19T18:10:49Z</time>
            </wpt>
            <wpt lat="37.97737636648677" lon="23.73767282406565">
                <ele>100.79</ele>
                <time>2023-03-19T18:11:00Z</time>
            </wpt>
            <wpt lat="37.97781613154516" lon="23.737318772475685">
                <ele>100.19</ele>
                <time>2023-03-19T18:11:14Z</time>
            </wpt>
            <wpt lat="37.97835874200748" lon="23.736901558870063">
                <ele>100.02</ele>
                <time>2023-03-19T18:11:32Z</time>
            </wpt>
            <wpt lat="37.97878581585577" lon="23.736520685189948">
                <ele>99.95</ele>
                <time>2023-03-19T18:11:46Z</time>
            </wpt>
            <wpt lat="37.97916923003795" lon="23.736212666619576">
                <ele>99.20</ele>
                <time>2023-03-19T18:11:58Z</time>
            </wpt>
            <wpt lat="37.97950327442217" lon="23.735939081300057">
                <ele>97.93</ele>
                <time>2023-03-19T18:12:09Z</time>
            </wpt>
            <wpt lat="37.979833088904776" lon="23.735660131562508">
                <ele>96.16</ele>
                <time>2023-03-19T18:12:20Z</time>
            </wpt>
            <wpt lat="37.98020618722079" lon="23.73537644403112">
                <ele>94.10</ele>
                <time>2023-03-19T18:12:32Z</time>
            </wpt>
            <wpt lat="37.98052754186235" lon="23.73514040963781">
                <ele>92.69</ele>
                <time>2023-03-19T18:12:42Z</time>
            </wpt>
            <wpt lat="37.98078102740884" lon="23.73490249876249">
                <ele>91.50</ele>
                <time>2023-03-19T18:12:51Z</time>
            </wpt>
            <wpt lat="37.98109815122494" lon="23.73465573553312">
                <ele>90.43</ele>
                <time>2023-03-19T18:13:01Z</time>
            </wpt>
            <wpt lat="37.98139836050812" lon="23.734366056959512">
                <ele>89.40</ele>
                <time>2023-03-19T18:13:11Z</time>
            </wpt>
            <wpt lat="37.98178736225963" lon="23.734124658148172">
                <ele>88.39</ele>
                <time>2023-03-19T18:13:23Z</time>
            </wpt>
            <wpt lat="37.98199877539012" lon="23.73390471700895">
                <ele>87.79</ele>
                <time>2023-03-19T18:13:30Z</time>
            </wpt>
            <wpt lat="37.98237931749014" lon="23.733577487509134">
                <ele>86.94</ele>
                <time>2023-03-19T18:13:43Z</time>
            </wpt>
            <wpt lat="37.98278945509948" lon="23.733298537771585">
                <ele>85.98</ele>
                <time>2023-03-19T18:13:56Z</time>
            </wpt>
            <wpt lat="37.98288247568165" lon="23.73313760523069">
                <ele>85.50</ele>
                <time>2023-03-19T18:14:00Z</time>
            </wpt>
            <wpt lat="37.98264992400512" lon="23.732729909460428">
                <ele>84.21</ele>
                <time>2023-03-19T18:14:11Z</time>
            </wpt>
            <wpt lat="37.98251884909906" lon="23.732509968321207">
                <ele>83.48</ele>
                <time>2023-03-19T18:14:17Z</time>
            </wpt>
            <wpt lat="37.98234760572384" lon="23.732180056612375">
                <ele>82.26</ele>
                <time>2023-03-19T18:14:25Z</time>
            </wpt>
            <wpt lat="37.9821319653496" lon="23.73177772526014">
                <ele>80.37</ele>
                <time>2023-03-19T18:14:35Z</time>
            </wpt>
            <wpt lat="37.98197974823332" lon="23.731512186567667">
                <ele>79.69</ele>
                <time>2023-03-19T18:14:42Z</time>
            </wpt>
            <wpt lat="37.98152206189242" lon="23.73192584679578">
                <ele>80.94</ele>
                <time>2023-03-19T18:14:58Z</time>
            </wpt>
            <wpt lat="37.98110768889182" lon="23.732263805131655">
                <ele>82.34</ele>
                <time>2023-03-19T18:15:12Z</time>
            </wpt>
            <wpt lat="37.980558006811066" lon="23.732714416246157">
                <ele>84.54</ele>
                <time>2023-03-19T18:15:30Z</time>
            </wpt>
            <wpt lat="37.97998143324145" lon="23.733175182919215">
                <ele>86.07</ele>
                <time>2023-03-19T18:15:49Z</time>
            </wpt>
            <wpt lat="37.97954590955547" lon="23.733641887287806">
                <ele>87.24</ele>
                <time>2023-03-19T18:16:05Z</time>
            </wpt>
            <wpt lat="37.97930066207053" lon="23.73376526890249">
                <ele>87.53</ele>
                <time>2023-03-19T18:16:12Z</time>
            </wpt>
            <wpt lat="37.97868904024683" lon="23.73438356131276">
                <ele>89.26</ele>
                <time>2023-03-19T18:16:34Z</time>
            </wpt>
            <wpt lat="37.97805491010762" lon="23.734882274981004">
                <ele>90.94</ele>
                <time>2023-03-19T18:16:55Z</time>
            </wpt>
            <wpt lat="37.97720453584287" lon="23.735627929087144">
                <ele>92.15</ele>
                <time>2023-03-19T18:17:24Z</time>
            </wpt>
            <wpt lat="37.97687048099584" lon="23.735880056734544">
                <ele>92.61</ele>
                <time>2023-03-19T18:17:35Z</time>
            </wpt>
            <wpt lat="37.976620996004385" lon="23.735863963480455">
                <ele>92.97</ele>
                <time>2023-03-19T18:17:42Z</time>
            </wpt>
            <wpt lat="37.975851392891364" lon="23.7357459462838">
                <ele>94.00</ele>
                <time>2023-03-19T18:18:04Z</time>
            </wpt>
            <wpt lat="37.975576532681075" lon="23.73570839535759">
                <ele>93.75</ele>
                <time>2023-03-19T18:18:11Z</time>
            </wpt>
            <wpt lat="37.97541584469677" lon="23.73568693768547">
                <ele>93.68</ele>
                <time>2023-03-19T18:18:15Z</time>
            </wpt>
            </gpx>
                        
                         
            """;
}
