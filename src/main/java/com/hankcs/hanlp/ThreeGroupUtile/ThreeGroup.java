package com.hankcs.hanlp.ThreeGroupUtile;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.seg.common.Term;
import com.springboot.pojo.ThreeTuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: zhongyuxinxi-02
 * @Date: 2018-12-03 11:14
 * @Description:
 */
 public class ThreeGroup {
    public static List<ThreeTuple> getThreeGroup(String text) {
//        Segment segment = HanLP.newSegment().enableNameRecognize(true);
//        List<String> strings = HanLP.extractSummary("沉井是下沉结构，必须掌握确凿的地质资料", 10);
//        List<Term> segment = BasicTokenizer.segment(strings.get(0));

//        String[] split = text.split("坏血症会导致患者疲惫、贫血、牙龈出血、牙齿脱落");
        List<Term> title_list = HanLP.segment(text);

//语义计算
//        getSimilarity("其实晚餐的最佳时间为六点半到七点左右","因为肠胃消化食物需要一定时间,晚餐时间超过八点就会有碍身体健康");
//        Segment segment = HanLP.newSegment();
//        List<List<Term>> lists = segment.seg2sentence(text, true);
//        System.out.println(lists);


        CoNLLSentence sentence = HanLP.parseDependency(text);


        System.out.println(sentence);

        //存放关系的集合
        List<Map<String, List<CoNLLWord>>> rootList = new ArrayList<Map<String, List<CoNLLWord>>>();
        Map<String, List<CoNLLWord>> map = new HashMap<String, List<CoNLLWord>>();
        // 可以方便地遍历它
        for (CoNLLWord word : sentence) {
            //依存关系id
            int id = word.ID;
//            循环每个实体与其他实体的关系
            for (CoNLLWord temp : sentence) {
                //存放相同关系的集合
                ArrayList<CoNLLWord> words = new ArrayList<CoNLLWord>();
                //判断word和temp是否存在关系
                if (temp.HEAD.ID == id) {
                    //如果map中存在已经存在关系，那么加入对应的list，没有就新加入
                    if (map.containsKey(temp.DEPREL)) {
                        List<CoNLLWord> coNLLWords = map.get(temp.DEPREL);
                        coNLLWords.add(temp);
                    } else {
                        words.add(temp);
                        map.put(temp.DEPREL, words);
                    }
                }
            }
//            System.out.println(map);
            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
//
        }
        rootList.add(map);
        CoNLLWord[] wordArray = sentence.getWordArray();
        //句法依存分析
        ArrayList<CoNLLWord> coNLLWords = new ArrayList<CoNLLWord>();
        for (int i = 0; i < wordArray.length; i++) {
            CoNLLWord coNLLWord = wordArray[i];
            coNLLWords.add(coNLLWord);
        }
        List<ThreeTuple> extract = extract(coNLLWords, rootList);
        System.out.println(extract);
        return extract;
    }

    /**
     * @param parser 句法依存分析
     * @param dict   词语依存字典
     * @return 三元组列表
     */
    private static List<ThreeTuple> extract(List<CoNLLWord> parser, List<Map<String, List<CoNLLWord>>> dict) {
        //返回值
        ThreeTuple tuple = new ThreeTuple();
        List<ThreeTuple> returnList = new ArrayList<>();
        //存放entity1的集合
        ArrayList<CoNLLWord> entity1List = new ArrayList<>();
        //存放entity1的集合
        ArrayList<CoNLLWord> entity2List = new ArrayList<>();
        //存放核心关系
        String rootrelation = "";
        for (CoNLLWord coNLLWord : parser) {
            if (coNLLWord.DEPREL.equals("核心关系")) {
                rootrelation = coNLLWord.LEMMA;
            }
        }
        //获取所有关系
        Map<String, List<CoNLLWord>> dic = dict.get(0);
        if (dic.containsKey("主谓关系") && dic.containsKey("动宾关系")) {
            //获取主语
            CoNLLWord entity1 = dic.get("主谓关系").get(0);
            //如果是动词则加入形容的实体
            if (entity1.CPOSTAG.equals("v")) {
                for (CoNLLWord word : parser) {
                    if (word.ID == entity1.ID + 1) {
                        entity1.LEMMA += word.NAME;
                    }
                }
            }
            entity1List.add(entity1);
            for (CoNLLWord coNLLWord : parser) {
                if (coNLLWord.HEAD.ID == entity1.ID && coNLLWord.DEPREL.equals("并列关系")) {
                    entity1List.add(coNLLWord);
                }
            }
            //循环动宾关系，把每个动宾作为entity2
            List<CoNLLWord> entryList = dic.get("动宾关系");
            for (CoNLLWord entity2 : entryList) {
                entity2List.add(entity2);
                for (CoNLLWord word : parser) {
                    //如果是形容词则加入形容的实体
                    if (entity2.ID - 1 == word.ID && word.CPOSTAG.equals("a")) {
                        entity2.LEMMA = word.NAME + entity2.LEMMA;
                    }
                    if (word.HEAD.ID == entity2.ID && word.DEPREL.equals("并列关系")) {//如果和动宾有并列关系，证明也是实体，所以也要加入
                        entity2List.add(word);
                    }
                }
            }

            for (CoNLLWord entityOne : entity1List) {
                for (CoNLLWord entityTow : entity2List) {
                    tuple.setEntityOne(entityOne.LEMMA);
                    tuple.setRelationship(rootrelation);
                    tuple.setEntityTow(entityTow.LEMMA);
                        returnList.add(tuple);
//                    result.add(entityOne.LEMMA + "," + rootrelation + "," + entityTow.LEMMA);


                }
            }


        } else if (dic.containsKey("主谓关系") && dic.containsKey("介宾关系")) {
            //获取主语
            CoNLLWord entity1 = dic.get("主谓关系").get(0);
            entity1List.add(entity1);
            for (CoNLLWord coNLLWord : parser) {
                if (coNLLWord.HEAD.ID == entity1.ID && coNLLWord.DEPREL.equals("并列关系")) {
                    entity1List.add(coNLLWord);
                }
            }
            CoNLLWord entity2 = dic.get("介宾关系").get(0);
            entity2List.add(entity2);
            for (CoNLLWord coNLLWord : parser) {
                if (coNLLWord.HEAD.ID == entity2.ID && coNLLWord.DEPREL.equals("并列关系")) {
                    entity2List.add(coNLLWord);
                }
            }
            for (CoNLLWord entityOne : entity1List) {
                for (CoNLLWord entityTow : entity2List) {
                    tuple.setEntityOne(entityOne.LEMMA);
                    tuple.setRelationship(entityOne.LEMMA);
                    tuple.setEntityTow(entityTow.LEMMA);
                    returnList.add(tuple);
                }
            }

        } else if (dic.containsKey("定中关系") && dic.containsKey("动宾关系")) {
            //获取主语
            CoNLLWord entity1 = dic.get("定中关系").get(0);
            for (CoNLLWord word : parser) {
                //如果是形容词则加入形容的实体
                if (entity1.ID + 1 == word.ID && word.DEPREL.equals("定中关系")) {
                    entity1.LEMMA += word.LEMMA;
                }
            }
            CoNLLWord entity2 = dic.get("动宾关系").get(0);
            tuple.setEntityOne(entity1.LEMMA);
            tuple.setRelationship(rootrelation);
            tuple.setEntityTow(entity2.LEMMA);
            returnList.add(tuple);
        } else if (dic.keySet().size() == 1 && dic.containsKey("定中关系")) {
            //处理只有定中关系和核心关系的语句
            //第一个词作为entity1
            CoNLLWord entity1 = parser.get(0);
            //最后一个分词作为entity2
            CoNLLWord entity2 = parser.get(parser.size() - 1);
            //entity1到entity2的动词名词 作为核心关系
            rootrelation = "";
            for (CoNLLWord coNLLWord : parser) {
                if (coNLLWord.ID < entity2.ID && coNLLWord.ID > entity1.ID) {
                    if (coNLLWord.POSTAG.equals("n") || coNLLWord.POSTAG.equals("v")) {
                        rootrelation += coNLLWord.NAME;
                    }
                }
            }
            tuple.setEntityOne(entity1.LEMMA);
            tuple.setRelationship(rootrelation);
            tuple.setEntityTow(entity2.LEMMA);
            returnList.add(tuple);
        } else if (dic.containsKey("状中结构") && dic.containsKey("介宾关系")) {
            CoNLLWord entity1 = null;
            CoNLLWord entity2;
            if (dic.containsKey("前置宾语")) {
                entity2 = dic.get("前置宾语").get(0);
                for (CoNLLWord word : parser) {
                    //如果有前置宾语，那么id加+2  书 id 1 由id=2 张灏id 3 编写  id=4
                    if (entity2.ID + 2 == word.ID) {
                        entity1 = word;
                    }
                }
                tuple.setEntityOne(entity1.LEMMA);
                tuple.setRelationship(rootrelation);
                tuple.setEntityTow(entity2.LEMMA);
                returnList.add(tuple);
            } else {

            }
        }
        return returnList;
    }


    public static double getSimilarity(String sentence1, String sentence2) {
        List<String> sent1Words = getSplitWords(sentence1);
        System.out.println(sent1Words);
        List<String> sent2Words = getSplitWords(sentence2);
        System.out.println(sent2Words);
        List<String> allWords = mergeList(sent1Words, sent2Words);

        int[] statistic1 = statistic(allWords, sent1Words);
        int[] statistic2 = statistic(allWords, sent2Words);

        double dividend = 0;
        double divisor1 = 0;
        double divisor2 = 0;
        for (int i = 0; i < statistic1.length; i++) {
            dividend += statistic1[i] * statistic2[i];
            divisor1 += Math.pow(statistic1[i], 2);
            divisor2 += Math.pow(statistic2[i], 2);
        }

        return dividend / (Math.sqrt(divisor1) * Math.sqrt(divisor2));
    }
    private static int[] statistic(List<String> allWords, List<String> sentWords) {
        int[] result = new int[allWords.size()];
        for (int i = 0; i < allWords.size(); i++) {
            result[i] = Collections.frequency(sentWords, allWords.get(i));
        }
        return result;
    }

    public static List<String> mergeList(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>();
        result.addAll(list1);
        result.addAll(list2);
        List<String> collect = result.stream().distinct().collect(Collectors.toList());
        return collect;
    }

    public static List<String> getSplitWords(String sentence) {
        return HanLP.segment(sentence).stream().map(a -> a.word).filter(s -> !"`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？ ".contains(s)).collect(Collectors.toList());
    }


}