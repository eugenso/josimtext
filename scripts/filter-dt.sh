spark-submit --class DTFilter --master=yarn-cluster --queue=shortrunning --num-executors 260 --driver-memory 7g --executor-memory 1g ~/noun-sense-induction_2.10-0.0.1.jar corpora/en/news100M_mwe_trigram__FreqSigLMI__PruneContext_s_0.0_w_10_f_10_wf_2_wpfmax_1000_wpfmin_2_p_1000__AggrPerFt__SimCount_sc_one_ac_True__SimSortWithFeatureslimit_200_minsim_2 dela-wiki-voc.csv dt-news-mwe-all-new true